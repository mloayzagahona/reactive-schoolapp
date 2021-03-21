package com.melg.schoolapp.service;

import com.melg.schoolapp.exception.MaximumAllowedCreditsException;
import com.melg.schoolapp.model.Record;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.model.Student;
import com.melg.schoolapp.model.Subject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRecordService {
  Flux<Record> fetchAllRecords();

  Mono<Record> addRecord(Record record) throws MaximumAllowedCreditsException;

  Mono<Record> updateRecord(Record record);

  Mono<Boolean> deleteRecord(Record record);

  Flux<Student> findEnrolledStudentsPerSubjectPerSemester(Subject record, Semester semester);

  Flux<Short> findCreditsPerSemesterPerStudent(Long studentId, Long semesterId);
}
