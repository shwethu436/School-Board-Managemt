package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScheduleNotFoundException extends RuntimeException {
  private String message;
}
