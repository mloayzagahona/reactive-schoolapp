package com.melg.schoolapp.service.impl;

import java.util.function.BiFunction;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.melg.schoolapp.dto.RecordDTO;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.model.Student;
import com.melg.schoolapp.repository.SemesterRepository;
import com.melg.schoolapp.repository.StudentRepository;
import com.melg.schoolapp.service.IStudentService;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StudentServiceImpl implements IStudentService {

	private final StudentRepository studentRepository;
	private final SemesterRepository semesterRepository;
	private final DatabaseClient databaseClient;

	public StudentServiceImpl(StudentRepository studentRepository, SemesterRepository semesterRepository,
			DatabaseClient databaseClient) {
		this.studentRepository = studentRepository;
		this.semesterRepository = semesterRepository;
		this.databaseClient = databaseClient;
	}

	@Override
	public Flux<Student> fetchAllStudents() {
		log.info("state=fetchAll, api=studentService");
		return studentRepository.findAll();
	}

	@Override
	public Mono<Student> fecthStudentById(Long studentId) {
		log.info("state=fecthStudentById, api=studentService");
		return studentRepository.findById(studentId);
	}

	@Override
	@Transactional
	public Mono<Student> addStudent(Student student) {
		log.info("state=create, api=studentService");
		return studentRepository.save(student);
	}

	@Override
	@Transactional
	public Mono<Student> updateStudent(Student student) {
		log.info("state=update, api=studentService");
		return studentRepository.findById(student.getStudentId()).flatMap(it -> {
			it.setFirstName(student.getFirstName());
			it.setLastName(student.getLastName());
			it.setNationality(student.getNationality());
			return studentRepository.save(it);
		}).switchIfEmpty(studentRepository.save(student));
	}

	@Override
	@Transactional
	public Mono<Void> enrollStudentToSemester(Student student) {
		log.info("state=enrollStudentToSemester, api=studentService");

		Mono<Semester> existingSemesterMono = semesterRepository.findById(student.getSemester().getSemesterId());

		Mono<Student> existingStudentMono = studentRepository.findById(student.getStudentId());

		return existingSemesterMono
				.zipWith(existingStudentMono,
						(semester, exStud) -> Student.builder().studentId(exStud.getStudentId())
								.firstName(exStud.getFirstName()).lastName(exStud.getLastName())
								.nationality(exStud.getNationality()).totalCredits(exStud.getTotalCredits())
								.semester(semester).build())
				.flatMap(it -> databaseClient
						.sql("UPDATE STUDENTS SET current_semester_id=:semesterId " + "WHERE student_id=:studentId;")
						.bind("semesterId", it.getSemester().getSemesterId()).bind("studentId", it.getStudentId())
						.then());
	}

	public static final BiFunction<Row, RowMetadata, RecordDTO> RECORD_MAPPING_FUNCTION = (row,
			rowMetaData) -> RecordDTO.builder().grade(row.get("grade", String.class))
					.name(row.get("name", String.class)).subjectId(row.get("subject_id", Integer.class)).build();

	public static final String SQL_FINDSUB_STUD_SEM = "SELECT r.subject_id as subject_id, s.name as name, r.grade as grade "
			+ "FROM RECORDS r INNER JOIN SUBJECTS s ON r.subject_id=s.subject_id "
			+ "WHERE r.student_id=:studentId and r.semester_id=:semesterId ";

	@Override
	public Flux<RecordDTO> findSubjectsPerStudentPerSemester(final Long studentId, final Long semesterId) {
		log.info("state=findSubjectsPerStudentPerSemester, api=studentService");

		return databaseClient.sql(SQL_FINDSUB_STUD_SEM).bind("studentId", studentId).bind("semesterId", semesterId)
				.map(RECORD_MAPPING_FUNCTION).all();
	}
}
