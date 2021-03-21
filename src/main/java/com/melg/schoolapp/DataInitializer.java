package com.melg.schoolapp;

import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.melg.schoolapp.model.Student;
import com.melg.schoolapp.model.Subject;
import com.melg.schoolapp.repository.StudentRepository;
import com.melg.schoolapp.repository.SubjectRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Builder
@RequiredArgsConstructor
class DataInitializer implements ApplicationRunner {

  private final StudentRepository students;
  private final SubjectRepository subjects;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("start data initialization...");
    this.students
        .saveAll(
            List.of(new Student("Manuel", "Loayza", "Peru", 0),
            		new Student("Dylan", "Loayza", "USA", 0)))
        .thenMany(this.students.findAll())
        .subscribe(
            data -> log.info("post:" + data),
            err -> log.error("error" + err),
            () -> log.info(" student initialization is done..."));
    this.subjects
        .saveAll(
            List.of(
                new Subject("Math I", "ma1", 4),
                new Subject("Math II", "ma2", 5),
                new Subject("Math III", "ma3", 5),
                new Subject("Physics I", "ph1", 4),
                new Subject("Physics II", "ph2", 5),
                new Subject("Chemestry I", "che3", 6),
                new Subject("English I", "eng1", 4)))
        .thenMany(this.subjects.findAll())
        .subscribe(
            data -> log.info("post:" + data),
            err -> log.error("error" + err),
            () -> log.info("subject initialization is done..."));
  }
}
