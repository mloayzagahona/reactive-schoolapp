package com.melg.schoolapp.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.melg.schoolapp.model.Student;

import reactor.core.publisher.Flux;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, Long> {

  @Query("SELECT * FROM Students WHERE last_name = :lastName")
  Flux<Student> findByLastName(String lastName);
}
