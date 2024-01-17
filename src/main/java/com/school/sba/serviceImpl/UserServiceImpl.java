package com.school.sba.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AdminAlreadyExsistException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.UserRepository;
import com.school.sba.service.UserService;
import com.school.sba.userDTO.UserRequest;
import com.school.sba.userDTO.UserResponse;
import com.school.sba.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ResponseStructure<UserResponse> structure;
	
	private User mapToUser(UserRequest request) {
		return User.builder()
				.userName(request.getUserName())
				.UserEmail(request.getUserEmail())
				.userFirstName(request.getUserFirstName())
				.userLastName(request.getUserLastName())
				.contactNo(request.getContactNo())
				.userPassword(request.getUserPassword())
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
	
	

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {
		if(userRequest.getUserRole()==UserRole.ADMIN && userRepo.existsByUserRole(UserRole.ADMIN)) {
			throw new AdminAlreadyExsistException("only one user is allowed with userRole as ADMIN");
		}else {
		User user1 = userRepo.save(mapToUser(userRequest));	
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("user registered successfully");
		structure.setData(mapToUserResponse(user1));
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
		User fetch1 = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user id is not exist"));
		userRepo.delete(fetch1);
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("user data deleted successfully");
		structure.setData(mapToUserResponse(fetch1));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
	}

}
