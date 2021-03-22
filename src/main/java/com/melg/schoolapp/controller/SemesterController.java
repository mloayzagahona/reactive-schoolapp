package com.melg.schoolapp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.service.ISemesterService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = SemesterController.SEMESTER_PATH)
public class SemesterController {
  public static final String SEMESTER_PATH = "/school/semester";

  private ISemesterService semesterService;

  public SemesterController(ISemesterService semesterService) {
    this.semesterService = semesterService;
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Semester> fetchAllSemesters() {
    return semesterService.fetchAllSemesters();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Semester> saveSemeter(@RequestBody Semester semester) {
    return semesterService.addSemester(semester);
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Semester> updateSemester(@RequestBody Semester semester) {
    if (semester.getSemesterId() == null) {
      return semesterService.addSemester(semester);
    }
    return semesterService.updateSemester(semester);
  }

}
