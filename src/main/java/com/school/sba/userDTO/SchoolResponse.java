package com.school.sba.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SchoolResponse {
	private int schoolId;
	private String schoolName;
	private String schoolEmail;
	private String schoolAddress;
	private long schoolContact;

}
