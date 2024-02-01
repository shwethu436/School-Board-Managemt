package com.school.sba.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AcademicNotFoundException;
import com.school.sba.exception.AdminAlreadyExsistException;
import com.school.sba.exception.AdminCanNotAssignedToAcademicProgram;
import com.school.sba.exception.AdminNotFoundException;
import com.school.sba.exception.OnlyTeacherCanBeAssignedToSubjectException;
import com.school.sba.exception.StudentCanNotAssignedToAcademicProgram;
import com.school.sba.exception.StudentNotFoundException;
import com.school.sba.exception.SubjectNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.service.UserService;
import com.school.sba.userDTO.UserRequest;
import com.school.sba.userDTO.UserResponse;
import com.school.sba.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ResponseStructure<UserResponse> structure;
	
	@Autowired
	private User user;
	
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@Autowired
	private ResponseStructure<List<UserResponse>> listStructure;
	

	
	private User mapToUser(UserRequest request) {
		return User.builder()
				.userName(request.getUserName())
				.UserEmail(request.getUserEmail())
				.userFirstName(request.getUserFirstName())
				.userLastName(request.getUserLastName())
				.contactNo(request.getContactNo())
				.userPassword(passwordEncoder.encode(request.getUserPassword()))
				.userRole(request.getUserRole())
				.build();
		}
	
	private UserResponse mapToUserResponse(User response) {
		return UserResponse.builder()
				.userId(response.getUserId())
				.userName(response.getUserName())
				.UserEmail(response.getUserEmail())
				.userFirstName(response.getUserFirstName())
				.userLastName(response.getUserLastName())
				.contactNo(response.getContactNo())
				.userRole(response.getUserRole())
				.build();
	}
	
	private School findAdminSchool() {
		User adminUser= userRepo.findByUserRole(UserRole.ADMIN);
		if(adminUser != null) {
			return adminUser.getSchool();
		}else {
			return null;
		}
			
		}
	

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {
		School adminSchool = findAdminSchool();
		if(userRequest.getUserRole()==UserRole.ADMIN && userRepo.existsByUserRole(UserRole.ADMIN)) {
			throw new AdminAlreadyExsistException("only one user is allowed with userRole as ADMIN");
		}else {
		User user1 = userRepo.save(mapToUser(userRequest));	
		
		//map teacher and student to the admin's school
		if(userRequest.getUserRole()==UserRole.TEACHER || userRequest.getUserRole()==UserRole.STUDENT) {
			user1.setSchool(adminSchool);
		}
		User savedUser = userRepo.save(user1);
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("user registered successfully");
		structure.setData(mapToUserResponse(savedUser));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> fetchUser(int userId) {
		User fetch1 = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user id is not exist"));
		structure.setStatusCode(HttpStatus.FOUND.value());
		structure.setMessage("user data fetched successfully");
		structure.setData(mapToUserResponse(fetch1));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {
		User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user id is not exist"));
		if(user.isDeleted())
			throw new UserNotFoundByIdException("user id is not exist");
		
		user.setDeleted(true);
		userRepo.save(user);
		
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("user data deleted successfully");
		structure.setData(mapToUserResponse(user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest) {
		if(userRepo.existsByUserRole(UserRole.ADMIN)) {
			throw new AdminAlreadyExsistException("only one user is allowed with userRole as ADMIN");
		}
		User user1 = userRepo.save(mapToUser(userRequest));	
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("user registered successfully");
		structure.setData(mapToUserResponse(user1));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> assignUser(int userId, int programId) {
		User user= userRepo.findById(userId)
				.orElseThrow(()-> new UserNotFoundByIdException("user does't exist"));
		
		AcademicProgram academicProgram = academicRepo.findById(programId)
				.orElseThrow(()-> new AcademicNotFoundException("academic program is not present"));
		
		if(user.getUserRole().equals(UserRole.ADMIN)) {
			throw new AdminCanNotAssignedToAcademicProgram("admin cannot assign");
		}else {
			if(user.getUserRole().equals(UserRole.TEACHER)) {
				if(academicProgram.getSubjects().contains(user.getSubject())) {
					user.getAcademicPrograms().add(academicProgram);
					userRepo.save(user);
					academicProgram.getUsers().add(user);
					academicRepo.save(academicProgram);
					
					structure.setStatusCode(HttpStatus.OK.value());
					structure.setMessage("teacher associated with academic program successfully");
					structure.setData(mapToUserResponse(user));
					return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
				}else {
					throw new SubjectNotFoundException("subject not found");
				}
			}else {
				throw new StudentCanNotAssignedToAcademicProgram("student cannot assign");
			}
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTheTeacher(int subjectId, int userId) {
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(()-> new StudentNotFoundException("subject not found"));
		
		User user = userRepo.findById(userId)
				.orElseThrow(()-> new UserNotFoundByIdException("user not found"));
		
		if(user.getUserRole().equals(UserRole.TEACHER )&& user.getSubject()== null) {
			user.setSubject(subject);
			userRepo.save(user);
			
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("subject added to the teacher successfully");
			structure.setData(mapToUserResponse(user));
			return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
		}else {
			throw new OnlyTeacherCanBeAssignedToSubjectException("user  is not teacher");
		}
	}

}







