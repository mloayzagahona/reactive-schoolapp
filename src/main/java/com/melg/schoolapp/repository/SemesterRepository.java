package com.melg.schoolapp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.melg.schoolapp.model.Semester;

@Repository
public interface SemesterRepository extends ReactiveCrudRepository<Semester, Long> {}
