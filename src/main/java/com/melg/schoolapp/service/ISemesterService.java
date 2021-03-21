package com.melg.schoolapp.service;

import com.melg.schoolapp.model.Semester;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISemesterService {
  Flux<Semester> fetchAllSemesters();

  Mono<Semester> addSemester(Semester semester);

  Mono<Semester> updateSemester(Semester semester);
}
