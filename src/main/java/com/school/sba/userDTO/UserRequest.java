package com.school.sba.userDTO;

import com.school.sba.enums.UserRole;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotEmpty(message="name can not be a null or blank")
    @Pattern(regexp="^[a-zA-Z0-9]+$",message="username must" 
            +"contain altleat 8 t0 20 characters with no specialcharacters")
    private String userName;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	
	private long contactNo;
	private String UserEmail;
	private UserRole userRole;

	
	
}
