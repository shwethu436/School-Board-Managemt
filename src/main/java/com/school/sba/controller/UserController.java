package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.service.UserService;
import com.school.sba.userDTO.UserRequest;
import com.school.sba.userDTO.UserResponse;
import com.school.sba.util.ResponseStructure;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody UserRequest userRequest){
		return userService.registerUser(userRequest);
	}
	
	@PostMapping("/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(@RequestBody UserRequest userRequest){
		return userService.registerAdmin(userRequest);
	}
	
	@GetMapping("/users/{userId}")
	
	public ResponseEntity<ResponseStructure<UserResponse>> fetchUser(@RequestBody int userId){
		return userService.fetchUser(userId);
	}
	
	@DeleteMapping("/users/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@RequestBody int userId){
		return userService.deleteUser(userId);
	} 

}
