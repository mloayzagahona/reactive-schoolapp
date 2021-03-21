package com.melg.schoolapp.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.melg.schoolapp.model.Base;
import com.melg.schoolapp.model.Semester;
import com.melg.schoolapp.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class StudentDTO extends Base {
  private String firstName;
  private String lastName;
  private String nationality;
  private String currentSemesterId;

  private List<Subject> lstEnrolledSubjects;
  private Semester semester;
}
