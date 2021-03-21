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
@Table("SEMESTERS")
public class Semester extends Base {
  @Id private Long semesterId;
  @NonNull private String name;
  @NonNull private int year;
  @NonNull private int period;
}
