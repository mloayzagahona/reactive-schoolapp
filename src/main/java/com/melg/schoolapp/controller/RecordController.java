package com.melg.schoolapp.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.melg.schoolapp.exception.MaximumAllowedCreditsException;
import com.melg.schoolapp.model.Record;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.model.Student;
import com.melg.schoolapp.model.Subject;
import com.melg.schoolapp.service.IRecordService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = RecordController.RECORD_PATH)
public class RecordController {

  public static final String RECORD_PATH = "/school/record";

  private IRecordService recordService;

  public RecordController(IRecordService recordService) {
    this.recordService = recordService;
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Record> fetchAllRecords() {
    return recordService.fetchAllRecords();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Record> saveRecord(@RequestBody Record record) throws MaximumAllowedCreditsException {
    return recordService.addRecord(record).onErrorMap(ex -> new MaximumAllowedCreditsException());
  }

  @PostMapping(
      value = "/{subjectId}/semester/{semesterId}/students",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Student> findEnrolledStudentsPerSubjectPerSemester(
      @PathVariable("subjectId") Long subjectId, @PathVariable("semesterId") Long semesterId) {
    Semester semester = new Semester();
    semester.setSemesterId(semesterId);
    Subject subject = new Subject();
    subject.setSubjectId(subjectId);
    return recordService.findEnrolledStudentsPerSubjectPerSemester(subject, semester);
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Record> updateRecord(@RequestBody Record record) {
    return recordService.updateRecord(record);
  }

  @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Boolean> deleteRecord(@RequestBody Record record) {
    return recordService.deleteRecord(record);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Maximum Allowed Credits Per Student")
  @ExceptionHandler(MaximumAllowedCreditsException.class)
  public Map<String, String> maximumAllowedCreditsExceptionHandler() {
    HashMap<String, String> response = new HashMap<>();
    response.put("message", "Maximum Allowed Credits Per Student");
    return response;
  }
}
