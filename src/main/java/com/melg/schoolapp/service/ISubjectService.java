package com.melg.schoolapp.service;

import com.melg.schoolapp.model.Subject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISubjectService {
  Flux<Subject> fetchAllSubjects();

  Mono<Subject> addSubject(Subject subject);

  Mono<Subject> updateSubject(Subject subject);
}
