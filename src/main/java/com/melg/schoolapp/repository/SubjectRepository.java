package com.melg.schoolapp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.melg.schoolapp.model.Subject;

@Repository
public interface SubjectRepository extends ReactiveCrudRepository<Subject, Long> {}
