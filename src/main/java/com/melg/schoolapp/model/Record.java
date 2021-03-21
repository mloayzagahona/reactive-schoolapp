package com.melg.schoolapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table("RECORDS")
public class Record extends Base {
  @Id private Long recordId;
  @NonNull private Long semesterId;
  @NonNull private Long subjectId;
  @NonNull private Long studentId;
  @NonNull private String grade;
}
