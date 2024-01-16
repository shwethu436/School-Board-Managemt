package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.User;
import com.school.sba.userDTO.UserRequest;
import com.school.sba.userDTO.UserResponse;
import com.school.sba.util.ResponseStructure;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> fetchUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);

}
