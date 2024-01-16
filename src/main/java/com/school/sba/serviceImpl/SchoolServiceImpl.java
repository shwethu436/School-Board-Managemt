package com.school.sba.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.service.SchoolService;
@Service
public class SchoolServiceImpl implements SchoolService{
    @Autowired
	private SchoolRepository schoolRepo;
    
	@Override
	public String saveData(int schoolId, String schoolName, String schoolEmail, String schoolAddress,
			long schoolContact) {
		School s1= new School();
		s1.setSchoolId(schoolId);
		s1.setSchoolName(schoolName);
		s1.setSchoolEmail(schoolEmail);
		s1.setSchoolAddress(schoolAddress);
		s1.setSchoolContact(schoolContact);
		schoolRepo.save(s1);
		
		return "data entered successfully";
	}

}
