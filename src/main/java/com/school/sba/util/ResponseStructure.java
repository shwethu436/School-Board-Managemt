package com.school.sba.util;

import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class ResponseStructure<T> {
	private int statusCode;
	private String message;
	private T data;

}
