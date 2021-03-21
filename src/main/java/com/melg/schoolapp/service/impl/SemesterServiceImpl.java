package com.melg.schoolapp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.repository.SemesterRepository;
import com.melg.schoolapp.service.ISemesterService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SemesterServiceImpl implements ISemesterService {

  private SemesterRepository semesterRepository;

  public SemesterServiceImpl(SemesterRepository semesterRepository) {
    this.semesterRepository = semesterRepository;
  }

  @Override
  public Flux<Semester> fetchAllSemesters() {
    log.info("state=fetchAllSemesters, api=semesterService");
    return semesterRepository.findAll();
  }

  @Override
  @Transactional
  public Mono<Semester> addSemester(Semester semester) {
    log.info("state=create, api=semesterService");
    return semesterRepository.save(semester);
  }

  @Override
  @Transactional
  public Mono<Semester> updateSemester(Semester semester) {
    log.info("state=update, api=semesterService");
    return semesterRepository
        .findById(semester.getSemesterId())
        .flatMap(
            it -> {
              it.setName(semester.getName());
              it.setPeriod(semester.getPeriod());
              it.setYear(semester.getYear());
              return semesterRepository.save(it);
            })
        .switchIfEmpty(semesterRepository.save(semester));
  }
}
