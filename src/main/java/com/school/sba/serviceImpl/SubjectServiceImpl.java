package com.school.sba.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Subject;
import com.school.sba.exception.AcademicNotFoundException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.service.SubjectService;
import com.school.sba.userDTO.AcademicResponse;
import com.school.sba.userDTO.SubjectRequest;
import com.school.sba.userDTO.SubjectResponse;
import com.school.sba.util.ResponseStructure;
@Service
public class SubjectServiceImpl implements SubjectService {
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@Autowired
	private ResponseStructure<AcademicResponse> structure;
	
	@Autowired
	private ResponseStructure<List<SubjectResponse>> subStructure;
	
	@Autowired
	private AcademicProgramServiceImpl academicServiceImpl;
	
	private SubjectResponse mapToSubjectResponse(Subject response) {
		return SubjectResponse.builder()
				.subjectId(response.getSubjectId())
				.subjectNames(response.getSubjectNames())
				.build();
	}
	
	
	

	@Override
	public ResponseEntity<ResponseStructure<AcademicResponse>> addSubject(int programId, SubjectRequest request) {
		
		return academicRepo.findById(programId).map(program->{ // found academic program
			List<Subject> subjects = (program.getSubjects()!=null)?program.getSubjects():new ArrayList<Subject>();
			//to add new subjects 
			request.getSubjectNames().forEach(name ->{
				boolean isPresent = false;
				for(Subject subject:subjects) {
					isPresent =(name.equalsIgnoreCase(subject.getSubjectNames()))?true:false;
					if(isPresent)break;
				}
				if(!isPresent)subjects.add(subjectRepo.findBySubjectNames(name)
						.orElseGet(()-> subjectRepo.save(Subject.builder().subjectNames(name).build())));
			});
			
			//to remove the subjects 
			List<Subject> toBeRemoved = new ArrayList<Subject>();
			subjects.forEach(subject ->{
				boolean isPresent = false;
				for(String name:request.getSubjectNames()) {
					isPresent =(subject.getSubjectNames().equalsIgnoreCase(name))?true:false;
					if(isPresent)break;
				}
				if(!isPresent)toBeRemoved.add(subject);
			});
			subjects.removeAll(toBeRemoved);
			
			program.setSubjects(subjects);  //set subjects list to the academic program
			academicRepo.save(program);
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("updated the subjects list");
			structure.setData(academicServiceImpl.mapToAcademicProgramResponse(program));
			
			return new ResponseEntity<ResponseStructure<AcademicResponse>>(structure,HttpStatus.CREATED);
		 }).orElseThrow(()-> new AcademicNotFoundException("AcademicProgram not found"));
	}




	@Override
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects() {
		List<Subject> findAll = subjectRepo.findAll();
		List<SubjectResponse> collect= findAll.stream()
				.map(u->mapToSubjectResponse(u))
				.collect(Collectors.toList());
		
		subStructure.setStatusCode(HttpStatus.FOUND.value());
		subStructure.setMessage("subjects found successfully");
		subStructure.setData(collect);
		return new ResponseEntity<ResponseStructure<List<SubjectResponse>>> (subStructure,HttpStatus.FOUND);
	}




	

}

