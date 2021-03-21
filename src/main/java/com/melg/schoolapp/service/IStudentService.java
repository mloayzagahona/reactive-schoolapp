package com.melg.schoolapp.service;

import com.melg.schoolapp.dto.RecordDTO;
import com.melg.schoolapp.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IStudentService {
  Flux<Student> fetchAllStudents();

  Mono<Student> addStudent(Student student);

  Mono<Student> updateStudent(Student student);

  Mono<Void> enrollStudentToSemester(Student student);

  Flux<RecordDTO> findSubjectsPerStudentPerSemester(Long studentId, Long semesterId);
  
}
