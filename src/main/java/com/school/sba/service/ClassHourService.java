package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.userDTO.ClassHourRequest;
import com.school.sba.userDTO.ClassHourResponse;
import com.school.sba.util.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId);

	ResponseEntity<String> updateClassHour(List<ClassHourRequest> request);

}
