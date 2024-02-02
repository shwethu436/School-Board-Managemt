package com.school.sba.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.ClassStatus;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AcademicNotFoundException;
import com.school.sba.exception.IllegleRequestException;
import com.school.sba.exception.InvalidClassHourException;
import com.school.sba.exception.OnlyTeacherCanBeAssignedToSubjectException;
import com.school.sba.exception.ScheduleNotFoundException;
import com.school.sba.exception.SubjectNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.service.ClassHourService;
import com.school.sba.userDTO.ClassHourRequest;
import com.school.sba.userDTO.ClassHourResponse;
import com.school.sba.util.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService{
	
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	@Autowired
	private ClassHourRepository classHourRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@Autowired
	private ResponseStructure<ClassHourResponse> structure;
	
	public ClassHourResponse mapToClassHourResponse(ClassHour response ) {
		return ClassHourResponse.builder()
				.classHourId(response.getClassHourId())
				.beginsAt(response.getBeginsAt())
				.endsAt(response.getEndsAt())
				.roomNo(response.getRoomNo())
				.classStatus(response.getClassStatus())
				.build();
	}
	
	private boolean isBreakTime(LocalDateTime beginsAt, LocalDateTime endsAt, Schedule schedule)
	{
		LocalTime breakTimeStart = schedule.getBreakTime();

		return ((breakTimeStart.isAfter(beginsAt.toLocalTime()) && breakTimeStart.isBefore(endsAt.toLocalTime())) || breakTimeStart.equals(beginsAt.toLocalTime()));
	}

	private boolean isLunchTime(LocalDateTime beginsAt, LocalDateTime endsAt , Schedule schedule)
	{
		LocalTime lunchTimeStart = schedule.getLunchTime();

		return ((lunchTimeStart.isAfter(beginsAt.toLocalTime()) && lunchTimeStart.isBefore(endsAt.toLocalTime())) || lunchTimeStart.equals(beginsAt.toLocalTime()));
	}




	@Override
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId) {
		return academicRepo.findById(programId)
				.map(academicProgarm -> {
					School school=academicProgarm.getSchool();
					Schedule schedule = school.getSchedule();
					if(schedule!=null)
					{
						int classHourPerDay = schedule.getClassHoursPerDay();
						int classHourLength = (int) schedule.getClassHourLengthInMin().toMinutes();

						LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpensAt());

						LocalDateTime lunchTimeStart = LocalDateTime.now().with(schedule.getLunchTime());
						LocalDateTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLengthInMin().toMinutes());
						LocalDateTime breakTimeStart = LocalDateTime.now().with(schedule.getBreakTime());
						LocalDateTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLengthInMin().toMinutes());

						for(int i=1;i<=6;i++) {
							for(int j=1;j<=classHourPerDay+2;j++) {
								ClassHour classHour = new ClassHour();
								LocalDateTime beginsAt = currentTime;
								LocalDateTime endsAt = beginsAt.plusMinutes(classHourLength);
								
								DayOfWeek dayOfWeek = beginsAt.getDayOfWeek();

								if(!isLunchTime(beginsAt, endsAt, schedule))
								{
									if(!isBreakTime(beginsAt, endsAt, schedule))
									{
										classHour.setBeginsAt(beginsAt);
										classHour.setEndsAt(endsAt);
										classHour.setClassStatus(ClassStatus.NOT_SCHEDULED);
										currentTime = endsAt;
									}
									else
									{
										classHour.setBeginsAt(breakTimeStart);
										classHour.setEndsAt(breakTimeEnd);
										classHour.setClassStatus(ClassStatus.BREAK_TIME);
										currentTime = breakTimeEnd;
									}
								}
								else
								{
									classHour.setBeginsAt(lunchTimeStart);
									classHour.setEndsAt(lunchTimeEnd);
									classHour.setClassStatus(ClassStatus.LUNCH_TIME);
									currentTime = lunchTimeEnd;
								}
								classHour.setDayOfWeek(dayOfWeek);

								classHour.setAList(academicProgarm);
								classHourRepo.save(classHour);
								structure.setStatusCode(HttpStatus.CREATED.value());
								structure.setMessage("Class hours created successfully!!!");
								structure.setData(mapToClassHourResponse(classHour));

							}
							currentTime = currentTime.plusDays(1).with(schedule.getOpensAt());
						}
					}
					else 
						throw new ScheduleNotFoundException("The School does not contain any schedule!!!");

					return new ResponseEntity<ResponseStructure<ClassHourResponse>>(structure,HttpStatus.CREATED);


				})
				.orElseThrow(() -> new AcademicNotFoundException("Invalid Program Id"));


	}

	
	 private void validateClassHours(List<ClassHourRequest> classHourRequestList) {
	        // Add any additional validation logic based on your requirements
	        if (classHourRequestList == null || classHourRequestList.isEmpty()) {
	            throw new InvalidClassHourException("Class hour list cannot be empty");
	        }
	     }

	@Override
	public ResponseEntity<String> updateClassHour(List<ClassHourRequest> listOfRequest) {
		validateClassHours(listOfRequest);

        List<ClassHour> classHoursToUpdate = new ArrayList<>();

        for (ClassHourRequest classHourRequest : listOfRequest) {
            Subject subject = subjectRepo.findById(classHourRequest.getSubjectId())
                    .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + classHourRequest.getSubjectId()));

            User teacher = userRepo
            		.findById(classHourRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundByIdException("User not found with id: " + classHourRequest.getUserId()));

            // Check for duplicate class hours
            if (classHourRepo.existsByRoomNoAndBeginsAtAndEndsAt(classHourRequest.getRoomNo(),
            		classHourRequest.getBeginsAt(), classHourRequest.getEndsAt())) {
                throw new InvalidClassHourException("Duplicate class hour found for room number "
                        + classHourRequest.getRoomNo() + " at date and time " + classHourRequest.getBeginsAt());
            }

            if(teacher.getUserRole().equals(UserRole.TEACHER)) {
            ClassHour classHour = classHourRepo.findById(classHourRequest.getClassHourId())
            		.orElseThrow(()->new IllegleRequestException("classhourId not found"));
            classHour.setSubject(subject);
            classHour.setUser(teacher);
            classHour.setRoomNo(classHourRequest.getRoomNo());
            classHour.setBeginsAt(classHourRequest.getBeginsAt());
            classHour.setEndsAt(classHourRequest.getEndsAt());
            classHour.setClassStatus(classHourRequest.getClassStatus());

            classHoursToUpdate.add(classHour);
            }
            else
            	throw new OnlyTeacherCanBeAssignedToSubjectException("User is not a Teacher!!");
        }

        classHourRepo.saveAll(classHoursToUpdate);
        return ResponseEntity.ok("Class hours updated successfully.");
	}

	@Override
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateNextWeekClassHour(int programId) {
        // Get existing ClassHour data from the database
        List<ClassHour> existingClassHours = classHourRepo.findAll();

        // Get the current date and time
        LocalDateTime currentDate = LocalDateTime.now();

        // Generate ClassHour instances for the next 6 days
        for (int day = 1; day <= 1; day++) {
        
            LocalDateTime nextDay = currentDate.plusDays(day);

            // Create new instances based on existing data
            List<ClassHour> newClassHours = existingClassHours.stream()
                    .map(existingClassHour -> {
                        ClassHour newClassHour = new ClassHour();
                        newClassHour.setBeginsAt(nextDay.withHour(existingClassHour.getBeginsAt().getHour())
                                .withMinute(existingClassHour.getBeginsAt().getMinute()));
                        newClassHour.setEndsAt(nextDay.withHour(existingClassHour.getEndsAt().getHour())
                                .withMinute(existingClassHour.getEndsAt().getMinute()));
                        newClassHour.setRoomNo(existingClassHour.getRoomNo());
                        newClassHour.setClassStatus(existingClassHour.getClassStatus());

                        Subject existingSubject = existingClassHour.getSubject();
                        if (existingSubject != null) {
                            // Reattach Subject entity to the persistence context using repository
                            Subject attachedSubject = subjectRepo.findById(existingSubject.getSubjectId())
                                    .orElseThrow(() -> new SubjectNotFoundException("Subject not found"));
                            newClassHour.setSubject(attachedSubject);
                        }

                        newClassHour.setAList(existingClassHour.getAList());
                        newClassHour.setUser(existingClassHour.getUser());
                        return newClassHour;
                    })
                    .collect(Collectors.toList());

            // Save the newly generated ClassHour instances
            classHourRepo.saveAll(newClassHours);
        }
      return null;
	}

}
