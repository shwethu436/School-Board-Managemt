package com.school.sba.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;

@Component
public class ScheduleJobs {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SchoolRepository schoolRepo;
	
	@Autowired
	private ClassHourRepository classHourRepo;
	
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	@Scheduled(fixedDelay = 1000l*60)
	public void test()
	{
		deleteUserIfDeleted();
		deleteSchoolIfDeleted();
		deleteAcademicProgramIfDeleted();
	}

	private void deleteUserIfDeleted() {
	    for (User user : userRepo.findAll()) {
	        if (!UserRole.ADMIN.equals(user.getUserRole()) && Boolean.TRUE.equals(user.isDeleted())) {
	            for (ClassHour classHour : user.getClassHours()) {
	                classHour.setUser(null);
	            }
	            classHourRepo.saveAll(user.getClassHours());

	            userRepo.delete(user);
	        }
	    }
	}

	public void deleteSchoolIfDeleted() {
	    List<School> schoolsToDelete = new ArrayList<>();

	    for (School school : schoolRepo.findAll()) {
	        if (school.isDeleted()) {
	            for (User user : school.getUList()) {
	                user.setSchool(null);
	            }
	            userRepo.saveAll(school.getUList());

	            for (AcademicProgram academicProgram : school.getAList()) {
	                academicProgram.setSchool(null);
	            }
	            academicRepo.saveAll(school.getAList());

	            schoolsToDelete.add(school);
	        }
	    }

	    schoolRepo.deleteAll(schoolsToDelete);
	}


	private void deleteAcademicProgramIfDeleted()
	{
		academicRepo.findAll().stream()
		.filter(AcademicProgram::isDeleted)
		.forEach(academicProgram -> 
		{
			//Deleting All the Class Hours related to the Academic Program
			if(!academicProgram.getClassHours().isEmpty())
				classHourRepo.deleteAll(academicProgram.getClassHours());

			academicProgram.getUsers().forEach(user -> user.setAcademicPrograms(null));
			userRepo.saveAll(academicProgram.getUsers());

			academicRepo.delete(academicProgram);
		});


	}

}
