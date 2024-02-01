package com.school.sba.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.exception.AcademicNotFoundException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.userDTO.AcademicRequest;
import com.school.sba.userDTO.AcademicResponse;
import com.school.sba.util.ResponseStructure;
@Service
public class AcademicProgramServiceImpl implements AcademicProgramService{
	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private AcademicProgramRepository academicRepo;
	@Autowired
	private ResponseStructure<AcademicResponse> structure;
	@Autowired
	private ResponseStructure<List<AcademicResponse>> listStructure;
	
	private AcademicProgram mapToAcademicProgram(AcademicRequest request) {
		return AcademicProgram.builder()
				.programName(request.getProgramName())
				.programType(request.getProgramType())
				.startsAt(request.getStartsAt())
				.endsAt(request.getEndsAt())
				.build();
	}
	
	AcademicResponse mapToAcademicProgramResponse(AcademicProgram response) {
		List<String>subjects = new ArrayList<String>();
		List<Subject> listOfSubjects= response.getSubjects();
		
		if(listOfSubjects!=null) {
			listOfSubjects.forEach(sub->{
				subjects.add(sub.getSubjectNames());
			});
		}
		return AcademicResponse.builder()
				.programId(response.getProgramId())
				.programName(response.getProgramName())
				.programType(response.getProgramType())
				.startsAt(response.getStartsAt())
				.endsAt(response.getEndsAt())
				.subjects(response.getSubjects())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicResponse>> addProgram(int schoolId, AcademicRequest request) {
		School school=schoolRepo.findById(schoolId).orElseThrow(()-> new SchoolNotFoundByIdException("school doesn't exist"));
		AcademicProgram academicProgram = academicRepo.save(mapToAcademicProgram(request));
		school.getAList().add(academicProgram);
		academicProgram.setSchool(school);
		schoolRepo.save(school);
		academicRepo.save(academicProgram);
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("academic program created succefully");
		structure.setData(mapToAcademicProgramResponse(academicProgram));
	
		return  new ResponseEntity<ResponseStructure<AcademicResponse>>(structure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicResponse>>> findProgram(int schoolId) {
		return schoolRepo.findById(schoolId).map(school->{
			List<AcademicProgram> aList = school.getAList();
			ResponseStructure<List<AcademicResponse>> rStructure=new ResponseStructure<>();
			List<AcademicResponse> l=new ArrayList<>();
			
			for(AcademicProgram a:aList) {
				l.add(mapToAcademicProgramResponse(a));
			}
			
			rStructure.setStatusCode(HttpStatus.FOUND.value());
			rStructure.setMessage("Academic programs found successfully!!!!");
			rStructure.setData(l);
			return new ResponseEntity<ResponseStructure<List<AcademicResponse>>>(rStructure,HttpStatus.FOUND);
			
		}).orElseThrow(()-> new SchoolNotFoundByIdException("School doesn't exist!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicResponse>> deleteProgram(int programId) {
        AcademicProgram academicProgram = academicRepo.findById(programId).orElseThrow(()-> new AcademicNotFoundException("Academic Program doesn't exist!!"));
		
        academicProgram.setDeleted(true);
        academicRepo.save(academicProgram);
		
	
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Academic programs deleted successfully!!!!");
		structure.setData(mapToAcademicProgramResponse(academicProgram));
		return new ResponseEntity<ResponseStructure<AcademicResponse>>(structure,HttpStatus.OK);
	}

	

}
