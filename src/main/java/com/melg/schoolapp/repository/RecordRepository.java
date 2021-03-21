package com.melg.schoolapp.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.melg.schoolapp.model.Record;
import reactor.core.publisher.Mono;

public interface RecordRepository extends ReactiveCrudRepository<Record, Long> {

  @Query(
      "SELECT * FROM RECORDS WHERE subject_id = :subjectId"
          + "student_id = :studentId and semester_id = :semesterId")
  Mono<Record> findRecordByFk(Long subjectId, Long studentId, Long semesterId);
}
