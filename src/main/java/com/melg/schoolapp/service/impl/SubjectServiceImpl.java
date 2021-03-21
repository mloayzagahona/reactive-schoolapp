package com.melg.schoolapp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.melg.schoolapp.model.Subject;
import com.melg.schoolapp.repository.SubjectRepository;
import com.melg.schoolapp.service.ISubjectService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SubjectServiceImpl implements ISubjectService {

  private SubjectRepository subjectRepository;
  public SubjectServiceImpl(SubjectRepository subjectRepository) {
    this.subjectRepository = subjectRepository;
  }
  @Override
  public Flux<Subject> fetchAllSubjects() {
    return subjectRepository.findAll();
  }

  @Override
  @Transactional
  public Mono<Subject> addSubject(Subject subject) {
    log.info("state=create, api=subjectService");
    return subjectRepository.save(subject);
  }

  @Override
  @Transactional
  public Mono<Subject> updateSubject(Subject subject) {
    log.info("state=update, api=subjectService");
    return subjectRepository
        .findById(subject.getSubjectId())
        .flatMap(
            it -> {
              it.setCode(subject.getCode());
              it.setCredits(subject.getCredits());
              it.setName(subject.getName());
              return subjectRepository.save(it);
            })  
        .switchIfEmpty(subjectRepository.save(subject));
  }

}
