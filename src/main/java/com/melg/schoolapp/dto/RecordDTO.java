package com.melg.schoolapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordDTO {
  private Integer subjectId;
  private String name;
  private String grade;
}
