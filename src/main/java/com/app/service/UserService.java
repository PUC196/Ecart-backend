package com.app.service;

import java.util.Optional;

import com.app.exceptions.APIException;
import com.app.pojo.User;

public interface UserService {
//public User findUserById(Long userId) throws APIException;
	
	public User findUserProfileByJwt(String jwt) throws APIException;
	

}
