package com.school.sba.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.IllegleREquestException;
import com.school.sba.exception.SchoolNotFoundByIdException;

import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.service.SchoolService;
import com.school.sba.userDTO.SchoolRequest;
import com.school.sba.userDTO.SchoolResponse;
import com.school.sba.util.ResponseStructure;
@Service
public class SchoolServiceImpl implements SchoolService{
    @Autowired
	private SchoolRepository schoolRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ResponseStructure<SchoolResponse> structure;
    
    private School mapToSchool(SchoolRequest request) {
    	return School.builder()
    			.schoolName(request.getSchoolName())
    			.schoolEmail(request.getSchoolEmail())
    			.schoolContact(request.getSchoolContact())
    			.schoolAddress(request.getSchoolAddress())
    			.build();
    }
    
    private SchoolResponse mapToSchoolResponse(School response) {
    	return SchoolResponse
    			.builder()
    			.schoolId(response.getSchoolId())
    			.schoolName(response.getSchoolName())
    			.schoolEmail(response.getSchoolEmail())
    			.schoolContact(response.getSchoolContact())
    			.schoolAddress(response.getSchoolAddress())
    			.build();
    }

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool( SchoolRequest request) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByUserName(userName).map(u->{
			if(u.getUserRole().equals(UserRole.ADMIN)) {
				if(u.getSchool()==null) {
					School school = mapToSchool(request);
					school=schoolRepo.save(school);  // saved new school
					u.setSchool(school);
					userRepo.save(u);  //updated the user with new school
					structure.setStatusCode(HttpStatus.CREATED.value());
					structure.setMessage("school saved successfully");
					structure.setData(mapToSchoolResponse(school));
					return  new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
				}else
					throw new IllegleREquestException("already exist");
			}else
				throw new IllegleREquestException("already exist");
		}).orElseThrow(()-> new UserNotFoundByIdException("failed to save school"));
		
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(int schoolId, SchoolRequest request) {
		School save = schoolRepo.findById(schoolId)
				.map(u ->{
					School mapToSchoolRequest = mapToSchool(request);
					mapToSchoolRequest.setSchoolId(schoolId);
					return schoolRepo.save(mapToSchoolRequest);
				})
				.orElseThrow(()-> new SchoolNotFoundByIdException("school not found"));
		
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("school updated successfully");
		structure.setData(mapToSchoolResponse(save));
		return  new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {
		School delete = schoolRepo.findById(schoolId)
				.orElseThrow(()-> new SchoolNotFoundByIdException("school not found"));
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("school deleted successfully");
		structure.setData(mapToSchoolResponse(delete));
		return  new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
	}
    
	

	
	
	
	
	
	
	
	
}
