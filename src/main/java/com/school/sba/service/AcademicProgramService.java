package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.userDTO.AcademicRequest;
import com.school.sba.userDTO.AcademicResponse;
import com.school.sba.util.ResponseStructure;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicResponse>> addProgram(int schoolId, AcademicRequest request);

	ResponseEntity<ResponseStructure<List<AcademicResponse>>> findProgram(int schoolId);

	ResponseEntity<ResponseStructure<AcademicResponse>> deleteProgram(int programId);

}
