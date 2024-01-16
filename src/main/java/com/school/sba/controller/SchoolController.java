package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.school.sba.service.SchoolService;

@Controller
public class SchoolController {
	
	@Autowired
	private SchoolService schoolServe;
	
	
	public String save(@RequestParam int schoolId,@RequestParam String schoolName,@RequestParam String schoolEmail,@RequestParam String schoolAdderess, @RequestParam long schoolContact) {
       return schoolServe.saveData(schoolId, schoolName, schoolEmail, schoolAdderess, schoolContact);
	}
}
