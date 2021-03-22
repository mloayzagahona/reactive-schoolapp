package com.melg.schoolapp.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import com.melg.schoolapp.dto.RecordDTO;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.model.Student;
import com.melg.schoolapp.repository.SemesterRepository;
import com.melg.schoolapp.repository.StudentRepository;
import com.melg.schoolapp.service.impl.StudentServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = StudentController.class)
@Import(StudentServiceImpl.class)
public class StudentControllerTest {
  @MockBean StudentRepository studentRepository;
  @MockBean SemesterRepository semesterRepository;
  @MockBean DatabaseClient databaseClient;
  @Autowired WebTestClient webClient;

  @BeforeEach
  public void setUp() {
    webClient = webClient.mutate().responseTimeout(Duration.ofMillis(3000000)).build();
  }

  @Test
  @DisplayName("Create Students")
  void testCreateStudent() {

    Student student =
        Student.builder()
            .firstName("Manuel")
            .lastName("Loayza")
            .nationality("Peru")
            .totalCredits(20)
            .build();

    Mockito.when(studentRepository.save(student)).thenReturn(Mono.just(student));

    webClient
        .post()
        .uri("/school/student")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(student))
        .exchange()
        .expectStatus()
        .isCreated();

    Mockito.verify(studentRepository, times(1)).save(student);
  }

  @Test
  @DisplayName("Update Student")
  void testUpdateStudent() {

    Student oldStudent =
        Student.builder()
            .studentId(Long.valueOf(1))
            .firstName("Manuel")
            .lastName("Loayza")
            .nationality("USA")
            .totalCredits(15)
            .build();

    Student newStudent =
        Student.builder()
            .studentId(Long.valueOf(1))
            .firstName("Manuel")
            .lastName("Loayza Gahona")
            .nationality("Peru")
            .totalCredits(15)
            .build();

    Mockito.when(studentRepository.findById(newStudent.getStudentId()))
        .thenReturn(Mono.just(oldStudent));

    Mockito.when(studentRepository.save(newStudent)).thenReturn(Mono.just(newStudent));

    webClient
        .put()
        .uri("/school/student")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(newStudent))
        .exchange()
        .expectStatus()
        .isOk();

    Mockito.verify(studentRepository, times(2)).save(newStudent);
    Mockito.verify(studentRepository, times(1)).findById(oldStudent.getStudentId());
  }

  @Test
  @DisplayName("Update Non-Existent Student")
  void testUpdateNonExistentStudent() {

    Student newStudent =
        Student.builder()
            .studentId(Long.valueOf(1))
            .firstName("Manuel")
            .lastName("Loayza Gahona")
            .nationality("Peru")
            .totalCredits(15)
            .build();

    Mockito.when(studentRepository.findById(newStudent.getStudentId())).thenReturn(Mono.empty());

    Mockito.when(studentRepository.save(newStudent)).thenReturn(Mono.just(newStudent));

    webClient
        .put()
        .uri("/school/student")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(newStudent))
        .exchange()
        .expectStatus()
        .isOk();

    Mockito.verify(studentRepository, times(1)).save(newStudent);
    Mockito.verify(studentRepository, times(1)).findById(newStudent.getStudentId());
  }

  @Test
  @DisplayName("Fetch All Students")
  void testFetchAllStudents() {
    var student =
        Student.builder()
            .firstName("Manuel")
            .lastName("Loayza")
            .nationality("Peru")
            .totalCredits(20)
            .build();

    var student2 =
        Student.builder()
            .firstName("Gerale")
            .lastName("Loayza")
            .nationality("Peru")
            .totalCredits(20)
            .build();

    List<Student> list = new ArrayList<>();
    list.add(student);
    list.add(student2);

    Flux<Student> studentFlux = Flux.fromIterable(list);

    Mockito.when(studentRepository.findAll()).thenReturn(studentFlux);

    webClient
        .get()
        .uri("/school/student/all")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Student.class)
        .hasSize(2);

    Mockito.verify(studentRepository, times(1)).findAll();
  }

//  @Test
//  @DisplayName("Find Subjects Per Student and Per Semester")
  void testFindSubjectsPerStudentPerSemester() {
    Semester semester = new Semester();
    semester.setSemesterId(Long.valueOf(1));

    Student student =
        Student.builder()
            .studentId(Long.valueOf(1))
            .firstName("Manuel")
            .lastName("Loayza")
            .nationality("Peru")
            .totalCredits(15)
            .build();
    student.setSemester(semester);

    var databaseClient = mock(DatabaseClient.class, RETURNS_MOCKS);

    RecordDTO dto = RecordDTO.builder().grade("3.4").name("Manuel").subjectId(1).build();

    Flux<RecordDTO> dtoFlux = Flux.just(dto);

    given(
            databaseClient
                .sql(StudentServiceImpl.SQL_FINDSUB_STUD_SEM)
                .bind("studentId", Long.valueOf(1))
                .bind("semesterId", Long.valueOf(1))
                .map(isA(BiFunction.class))
                .all())
        .willReturn(dtoFlux);

    webClient
        .post()
        .uri("/school/student/1/subjects/semester/1")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(RecordDTO.class)
        .hasSize(1);
  }
}
