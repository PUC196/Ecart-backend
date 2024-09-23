package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.exceptions.APIException;
import com.app.pojo.User;
import com.app.service.UserService;

@RestController
@CrossOrigin( origins = "http://localhost:3000")   
@RequestMapping("/api/users")
public class UserController {
 @Autowired
 private UserService userService;
 
 @GetMapping("/profile")
 public ResponseEntity<User>getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws APIException {
	 
	 User user=userService.findUserProfileByJwt(jwt);
	 
	 return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
 }
}
