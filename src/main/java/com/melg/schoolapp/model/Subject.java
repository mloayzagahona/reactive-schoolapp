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
@Table("SUBJECTS")
public class Subject extends Base {
  @Id private Long subjectId;
  @NonNull private String name;
  @NonNull private String code;
  @NonNull private int credits;
}
