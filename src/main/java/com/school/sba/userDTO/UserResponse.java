package com.school.sba.userDTO;

import com.school.sba.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserResponse {
	private int userId;
	private String userName;
	private String userFirstName;
	private String userLastName;
	private long contactNo;
	private String UserEmail;
	private UserRole userRole;

}
