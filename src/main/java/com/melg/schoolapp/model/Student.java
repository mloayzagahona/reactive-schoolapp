package com.melg.schoolapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("STUDENTS")
@JsonInclude(Include.NON_NULL)
public class Student extends Base  implements Persistable<Long> {
  @Id private Long studentId;
  @NonNull private String firstName;
  @NonNull private String lastName;
  @NonNull private String nationality;
  @NonNull private int totalCredits;
  
  @Column("current_semester_id")
  private String currentSemesterId;
  
  @Getter(value = AccessLevel.NONE)
  @Setter(value = AccessLevel.NONE)
  @Transient
  private Semester semester;

  public Semester getSemester() {
    return semester;
  }

  public void setSemester(Semester semester) {
    this.semester = semester;
  }
  
  //Student(Long, String, String, String, null, Semester)
  
  @Transient
  private boolean newStudent;

  @Override
  @Transient
  public boolean isNew() {
      return this.newStudent || studentId == null;
  }

  public Student setAsNew() {
      this.newStudent = true;
      return this;
  }

  @Override
  public Long getId() {
    return this.studentId;
  }
}
