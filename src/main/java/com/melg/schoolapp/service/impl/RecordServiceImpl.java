package com.melg.schoolapp.service.impl;

import java.util.function.BiFunction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.melg.schoolapp.exception.MaximumAllowedCreditsException;
import com.melg.schoolapp.model.Record;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.model.Student;
import com.melg.schoolapp.model.Subject;
import com.melg.schoolapp.repository.RecordRepository;
import com.melg.schoolapp.repository.StudentRepository;
import com.melg.schoolapp.repository.SubjectRepository;
import com.melg.schoolapp.service.IRecordService;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RecordServiceImpl implements IRecordService {

  private RecordRepository recordRepository;
  private DatabaseClient databaseClient;
  private SubjectRepository subjectRepository;
  private StudentRepository studentRepository;
  
  @Value("${student.credits.total.max:20}")
  private int maximumCreditsPerStudent;

  public RecordServiceImpl(
      RecordRepository recordRepository,
      DatabaseClient databaseClient,
      SubjectRepository subjectRepository,
      StudentRepository studentRepository) {
    this.recordRepository = recordRepository;
    this.databaseClient = databaseClient;
    this.subjectRepository = subjectRepository;
    this.studentRepository = studentRepository;
  }

  @Override
  public Flux<Record> fetchAllRecords() {
    log.info("state=fetchAll, api=recordService");
    return recordRepository.findAll();
  }

  @Override
  @Transactional
  public Mono<Record> addRecord(final Record record) throws MaximumAllowedCreditsException {
    log.info("state=create, api=recordService");
    // validation rule:
    // no more 20 credits per semester per student
    Mono<Subject> subjectMono = subjectRepository.findById(record.getSubjectId());

    Mono<Integer> subTotalCreditsMono =
        findCreditsPerSemesterPerStudent(record.getStudentId(), record.getSemesterId())
            .reduce(0, (x1, x2) -> x1 + x2);

    return subjectMono
        .zipWith(
            subTotalCreditsMono,
            (subject, subTotalCredits) -> {
              int totalCredits = subject.getCredits() + subTotalCredits;
              if (totalCredits > this.maximumCreditsPerStudent) {
                throw Exceptions.propagate(new MaximumAllowedCreditsException());
              }
              return recordRepository.save(record).doOnSuccess(t -> {
                Student stud = studentRepository.findById(record.getStudentId()).block();
                stud.setTotalCredits(totalCredits);
                studentRepository.save(stud).block();
              });
            })
        .block();
  }

  @Override
  @Transactional
  public Mono<Record> updateRecord(Record record) {
    log.info("state=update, api=recordService");
    return recordRepository
        .findById(record.getRecordId())
        .flatMap(
            it -> {
              it.setSemesterId(record.getSemesterId());
              it.setStudentId(record.getStudentId());
              it.setSubjectId(record.getSubjectId());
              it.setGrade(record.getGrade());
              return recordRepository.save(it);
            })
        .switchIfEmpty(recordRepository.save(record));
  }

  public static final BiFunction<Row, RowMetadata, Short> CREDITS_MAPPING_FUNCTION =
      (row, rowMetaData) -> row.get("credits", Short.class);

  public static final BiFunction<Row, RowMetadata, Student> STUDENT_MAPPING_FUNCTION =
      (row, rowMetaData) ->
          Student.builder()
              .firstName(row.get("first_name", String.class))
              .lastName(row.get("last_name", String.class))
              .nationality(row.get("nationality", String.class))
              .build();

  @Override
  public Flux<Student> findEnrolledStudentsPerSubjectPerSemester(
      Subject subject, Semester semester) {
    log.info("state=findEnrolledStudentsPerSubjectPerSemester, api=recordService");

    return databaseClient
        .sql(
            "SELECT s.student_id as student_id, s.first_name as first_name, "
                + "s.last_name as last_name, s.nationality as nationality "
                + "FROM RECORDS r INNER JOIN STUDENTS s ON r.student_id=s.student_id "
                + "WHERE r.subject_id=:subjectId and r.semester_id=:semesterId ")
        .bind("subjectId", subject.getSubjectId())
        .bind("semesterId", semester.getSemesterId())
        .map(STUDENT_MAPPING_FUNCTION)
        .all();
  }

  @Override
  public Flux<Short> findCreditsPerSemesterPerStudent(Long studentId, Long semesterId) {
    log.info("state=findCreditsPerSemesterPerStudent, api=recordService");

    return databaseClient
        .sql(
            "SELECT s.credits as credits "
                + "FROM RECORDS r INNER JOIN SUBJECTS s ON r.subject_id=s.subject_id "
                + "WHERE r.student_id=:studentId and r.semester_id=:semesterId ")
        .bind("studentId", studentId)
        .bind("semesterId", semesterId)
        .map(CREDITS_MAPPING_FUNCTION)
        .all();
  }

  @Override
  @Transactional
  public Mono<Boolean> deleteRecord(Record record) {
    log.info("state=deleteRecord, api=recordService");
    return recordRepository
        .findRecordByFk(record.getRecordId(), record.getStudentId(), record.getSemesterId())
        .flatMap(it -> recordRepository.delete(it).then(Mono.just(Boolean.TRUE)));
  }
}
