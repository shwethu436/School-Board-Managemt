package com.school.sba.userDTO;

import java.time.Duration;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ScheduleResponse {
	private int scheduleId;
	private LocalTime opensAt;
	private LocalTime closesAt;
	private int classHourLengthInMin;
	private LocalTime breakTime;
	private int breakLengthInMin;
	private LocalTime lunchTime;
	private int lunchLengthInMin;
	private int classHoursPerDay;

}
