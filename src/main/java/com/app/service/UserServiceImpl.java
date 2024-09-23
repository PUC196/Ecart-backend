package com.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.security.jwt.*;
import com.app.exceptions.APIException;
import com.app.pojo.User;
import com.app.repository.UserRepository;

@Service
public class UserServiceImpl  implements UserService{
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private JwtUtils jwtUtils;

//	@Override
//	public User findUserById(Long userId) throws APIException {
//		Optional<User>user=userRepo.findById(userId);
//		if(user.isPresent())
//			return user.get();
//		throw new APIException("user not found with id "+userId);
//	}

	@Override
	public User findUserProfileByJwt(String jwt) throws APIException {
		System.out.println("jwt before"+jwt);
		// If the token starts with "Bearer ", remove it
	    if (jwt.startsWith("Bearer ")) {
	        jwt = jwt.substring(7); // Remove the "Bearer " prefix
	    }
	    String email = jwtUtils.getUserNameFromJwtToken(jwt);
	    System.out.println("jwt after"+jwt);
	    
	    Optional<User> userOptional = userRepo.findByEmail(email);
	    
	    // Use orElseThrow to throw an exception if the user is not found
	    User user = userOptional.orElseThrow(() -> new APIException("User not found with email: " + email));
	    
	    // No need for the null check as orElseThrow handles that
	    return user;
	}

//	public User findUserProfileByJwt(String jwt) throws APIException {
//		String email=jwtUtils.getUserNameFromJwtToken(jwt);
//		
//		Optional<User> userOptional = userRepo.findByEmail(email);
//		User user = userOptional.orElseThrow(() -> new APIException("User not found with email: " + email));
//
//		
//		if(user==null)
//			throw new APIException("user not found with email "+email);
//		
//		return user;
//	}
//	

}
