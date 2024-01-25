package com.school.sba.serviceImpl;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.exception.IllegleREquestException;
import com.school.sba.exception.ScheduleNotFoundException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.ScheduleRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.service.ScheduleService;
import com.school.sba.userDTO.ScheduleRequest;
import com.school.sba.userDTO.ScheduleResponse;
import com.school.sba.util.ResponseStructure;
@Service
public class ScheduleServiceImpl implements ScheduleService{
	@Autowired
	private ScheduleRepository scheduleRepo;
	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private ResponseStructure<ScheduleResponse> structure;
	
	private Schedule mapToSchedule(ScheduleRequest request) {
		return Schedule.builder()
				.opensAt(request.getOpensAt())
				.closesAt(request.getClosesAt())
				.breakTime(request.getBreakTime())
				.classHoursPerDay(request.getClassHoursPerDay())
				.classHourLengthInMin(Duration.ofMinutes(request.getClassHourLengthInMin()))
				.breakLengthInMin(Duration.ofMinutes(request.getBreakLengthInMin()))
				.lunchLengthInMin(Duration.ofMinutes(request.getLunchLengthInMin()))
				.lunchTime(request.getLunchTime())
				.build();
	}
	
	private ScheduleResponse mapToScheduleResponse(Schedule response) {
		return ScheduleResponse.builder()
				.scheduleId(response.getScheduleId())
				.opensAt(response.getOpensAt())
				.closesAt(response.getClosesAt())
				.breakTime(response.getBreakTime())
				.classHoursPerDay(response.getClassHoursPerDay())
				.lunchTime(response.getLunchTime())
				.classHourLengthInMin((int)(Duration.ofMinutes(response.getClassHourLengthInMin().toMinutes()).toMinutes()))
				.breakLengthInMin((int)(Duration.ofMinutes(response.getBreakLengthInMin().toMinutes()).toMinutes()))
				.lunchLengthInMin((int)(Duration.ofMinutes(response.getLunchLengthInMin().toMinutes()).toMinutes()))
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(int schoolId, ScheduleRequest request) {
		School school= schoolRepo.findById(schoolId)
				.orElseThrow(()-> new SchoolNotFoundByIdException("school not found"));
			if(school.getSchedule()==null) {
				Schedule schedule = scheduleRepo.save(mapToSchedule(request)) ;
				
				school.setSchedule(schedule);
				schoolRepo.save(school);
				structure.setStatusCode(HttpStatus.CREATED.value());
				structure.setMessage("scheduled added successfully");
				structure.setData(mapToScheduleResponse(schedule));
				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.CREATED) ;
				
			}else
				throw new IllegleREquestException("school can schedule only one schedule");
		
   }

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int schoolId) {
		   School school = schoolRepo.findById(schoolId).orElseThrow(()-> new SchoolNotFoundByIdException("school not found"));
		   Schedule schedule = school.getSchedule();
		   int scheduleId=schedule.getScheduleId();
		   Optional<Schedule> findById= scheduleRepo.findById(scheduleId);
		   schedule= findById.get();
			structure.setStatusCode(HttpStatus.FOUND.value());
			structure.setMessage("scheduled found successfully");
			structure.setData(mapToScheduleResponse(schedule));
			return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.FOUND) ;
		
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest request) {
		Schedule schedule=scheduleRepo.findById(scheduleId).map(u->{
			Schedule updated= mapToSchedule(request);
			updated.setScheduleId(u.getScheduleId());
			return scheduleRepo.save(updated);
		}).orElseThrow(()-> new ScheduleNotFoundException("Schedule not found"));
		
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("scheduled updated successfully");
		structure.setData(mapToScheduleResponse(schedule));
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.OK) ;
	}
}

















	
