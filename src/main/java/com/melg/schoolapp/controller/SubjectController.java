package com.melg.schoolapp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.melg.schoolapp.model.Subject;
import com.melg.schoolapp.service.ISubjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = SubjectController.SUBJECT_PATH)
public class SubjectController {

  public static final String SUBJECT_PATH = "/school/subject";

  private ISubjectService subjectService;
  
  public SubjectController(ISubjectService subjectService) {
    this.subjectService = subjectService;
  }
  
  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Subject> fetchAllSubjetcs() {
    return subjectService.fetchAllSubjects();
  }
  
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Subject> saveSubject(@RequestBody Subject subject) {
    return subjectService.addSubject(subject);
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Subject> updateSubject(@RequestBody Subject subject) {
    return subjectService.updateSubject(subject);
  }
  
  
}
