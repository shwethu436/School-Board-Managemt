package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.userDTO.AcademicResponse;
import com.school.sba.userDTO.SubjectRequest;
import com.school.sba.userDTO.SubjectResponse;
import com.school.sba.util.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<AcademicResponse>> addSubject(int programId, SubjectRequest request);

	

}
