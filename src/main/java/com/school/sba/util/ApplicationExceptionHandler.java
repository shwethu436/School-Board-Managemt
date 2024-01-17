package com.school.sba.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.school.sba.exception.AdminAlreadyExsistException;
import com.school.sba.exception.UserNotFoundByIdException;
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{
	
	private  ResponseEntity<Object> structure(HttpStatus status, String message, Object rootCause){
		return new ResponseEntity<Object>(Map.of(
				"Status",status.value(),
				"Message",message,
				"RootCause",rootCause
				),status);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
	    List<ObjectError> allErrors = ex.getAllErrors();
	    Map<String, String> errors=new HashMap<String,String>();
	    allErrors.forEach(error->{
	    	FieldError fieldError = (FieldError)error;
	    	errors.put(fieldError.getField(),fieldError.getDefaultMessage());
	    });
	    return structure(HttpStatus.BAD_REQUEST,"failed to save the data", errors);
	}
	
	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<Object> handleUserNotFoundById(UserNotFoundByIdException ex){
		return structure(HttpStatus.NOT_FOUND,ex.getMessage(),"user not found with the given id");
	}
	@ExceptionHandler(AdminAlreadyExsistException.class)
	public ResponseEntity<Object> handleRuntime(AdminAlreadyExsistException aex){
		return structure(HttpStatus.NOT_ACCEPTABLE,aex.getMessage(),"Admin already present");
	}
}
