package com.example.Ecommerce_BE.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.model.entity.ERole;
import com.example.Ecommerce_BE.model.entity.Roles;
import com.example.Ecommerce_BE.model.entity.Users;
import com.example.Ecommerce_BE.model.service.RoleService;
import com.example.Ecommerce_BE.model.service.UserService;
import com.example.Ecommerce_BE.payload.request.SignupRequest;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PasswordEncoder encoder;
	
	@PostMapping("/create/moderator")
	public ResponseEntity<?> createModerator(@RequestBody SignupRequest signupRequest)
	{
		if(userService.existsByUserName(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already"));
		}
		if(userService.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: email is already"));
		}
		
		Users user = new Users();
		user.setUsername(signupRequest.getUsername());
		user.setPassword(encoder.encode(signupRequest.getPassword()));
		user.setEmail(signupRequest.getEmail());
		List<Roles> listRoles = new ArrayList<>();
		// set role
		Optional<Roles> userRole= roleService.findByRoleName(ERole.ROLE_USER);
		listRoles.add(userRole.get());
		userRole= roleService.findByRoleName(ERole.ROLE_MODERATOR);
		listRoles.add(userRole.get());
				
		user.setListRoles(listRoles);
		userService.saveOrUpdate(user);
		return ResponseEntity.ok( new MessageResponse("Success: Create successfully"));
	}
	@PostMapping("/create/admin")
	public ResponseEntity<?> createAdmin(@RequestBody SignupRequest signupRequest)
	{
		if(userService.existsByUserName(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already"));
		}
		if(userService.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: email is already"));
		}
		
		Users user = new Users();
		user.setUsername(signupRequest.getUsername());
		user.setPassword(encoder.encode(signupRequest.getPassword()));
		user.setEmail(signupRequest.getEmail());
		List<Roles> listRoles = new ArrayList<>();
		// set role
		Optional<Roles> userRole= roleService.findByRoleName(ERole.ROLE_USER);
		listRoles.add(userRole.get());
		userRole= roleService.findByRoleName(ERole.ROLE_MODERATOR);
		listRoles.add(userRole.get());
		userRole= roleService.findByRoleName(ERole.ROLE_ADMIN);
		listRoles.add(userRole.get());
				
		user.setListRoles(listRoles);
		userService.saveOrUpdate(user);
		return ResponseEntity.ok( new MessageResponse("Success: Create successfully"));
	}
}
