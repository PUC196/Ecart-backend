package com.app.service;

import com.app.security.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> registerUser(SignupRequest signUpRequest);
}
