package com.melg.schoolapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.H2Dialect;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.melg.schoolapp.model.Student;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class StudentRepositoryTest {

  R2dbcEntityTemplate template;

  @Autowired private DatabaseClient databaseClient;

  @Autowired private StudentRepository repository;

  @BeforeEach
  public void setup() {
    initializeDatabase();
  }

  private void initializeDatabase() {
    this.template = new R2dbcEntityTemplate(databaseClient, H2Dialect.INSTANCE);

    String query =
        "CREATE TABLE IF NOT EXISTS STUDENTS (student_id SERIAL PRIMARY KEY, first_name VARCHAR(255), "
            + "last_name VARCHAR(255), nationality VARCHAR(255), current_semester_id int, "
            + "    total_credits int NOT NULL)";
    template.getDatabaseClient().sql(query).fetch().rowsUpdated().block();
  }

  @Test
  void testFindByLastName() {
    Student student = new Student("first", "last", "America", 0);

    template.insert(Student.class).using(student).then().as(StepVerifier::create).verifyComplete();

    Flux<Student> findByLastName = repository.findByLastName(student.getLastName());

    findByLastName
        .as(StepVerifier::create)
        .assertNext(
            actual -> {
              assertThat(actual.getFirstName()).isEqualTo("first");
              assertThat(actual.getLastName()).isEqualTo("last");
            })
        .verifyComplete();
  }
}
