package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.userDTO.ScheduleRequest;
import com.school.sba.userDTO.ScheduleResponse;
import com.school.sba.util.ResponseStructure;

public interface ScheduleService {

	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(int schoolId,ScheduleRequest request) ;

	public ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int scheduleId);

	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest request);
		

}
