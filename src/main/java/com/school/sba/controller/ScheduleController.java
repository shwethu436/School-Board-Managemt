package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.service.ScheduleService;
import com.school.sba.userDTO.ScheduleRequest;
import com.school.sba.userDTO.ScheduleResponse;
import com.school.sba.util.ResponseStructure;

@RestController
public class ScheduleController {
	@Autowired
	private ScheduleService scheduleServe;
	
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(@PathVariable int schoolId,@RequestBody ScheduleRequest request){
		
		return scheduleServe.addSchedule(schoolId,request);
	}
	
	@GetMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(@PathVariable int schoolId){
		return scheduleServe.findSchedule(schoolId);
	}
	
	@PutMapping("schedules/{scheduleId}")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(@PathVariable int scheduleId, @RequestBody ScheduleRequest request){
		return scheduleServe.updateSchedule(scheduleId, request);
	}
	

}
