package com.example.Ecommerce_BE.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.ERole;
import com.example.Ecommerce_BE.model.entity.Roles;
import com.example.Ecommerce_BE.model.entity.Users;
import com.example.Ecommerce_BE.model.service.RoleService;
import com.example.Ecommerce_BE.model.service.UserService;
import com.example.Ecommerce_BE.payload.request.LoginRequest;
import com.example.Ecommerce_BE.payload.request.SignupRequest;
import com.example.Ecommerce_BE.payload.response.JwtRespone;
import com.example.Ecommerce_BE.payload.response.MessageResponse;
import com.example.Ecommerce_BE.security.CustomerUserDetail;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PasswordEncoder encoder;
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest)
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
		List<String> strRoles= signupRequest.getListRoles();
		List<Roles> listRoles = new ArrayList<>();
		if(strRoles == null)
		{
			Optional<Roles> userRole= roleService.findByRoleName(ERole.ROLE_USER);
			listRoles.add(userRole.get());
		}
		else
		{
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Optional<Roles> adminRole=roleService.findByRoleName(ERole.ROLE_ADMIN);
						listRoles.add(adminRole.get());
					case "moderator":
						Optional<Roles> moderatorRole=roleService.findByRoleName(ERole.ROLE_MODIRATOR);
						listRoles.add(moderatorRole.get());
					case "user":
						
//						Optional<Roles> userRole=roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(()-> RuntimeException("Error: Role is not found"));
						Optional<Roles> userRole=roleService.findByRoleName(ERole.ROLE_USER);
						listRoles.add(userRole.get());
				}
			});
		}
		user.setListRoles(listRoles);
		userService.saveOrUpdate(user);
		return ResponseEntity.ok( new MessageResponse("create successfully"));
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest)
	{
//		System.out.println("log 1");
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//		);
//		System.out.println("log 2");
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		System.out.println("log 3");
//		CustomerUserDetail customerUserDetail = (CustomerUserDetail) authentication.getPrincipal();
//		System.out.println("log 4");
//		String jwt =  tokenProvider.genarateToken(customerUserDetail);
//		System.out.println("log 5");
//		List<String> listRoles= customerUserDetail.getAuthorities().stream()
//									.map(item->item.getAuthority()).collect(Collectors.toList());
//		System.out.println("log 6");
//		return ResponseEntity.ok(new JwtRespone(jwt, customerUserDetail.getUsername(), customerUserDetail.getEmail(), listRoles));
		 try {
		        Authentication authentication = authenticationManager.authenticate(
		                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
		        );
		        // Các bước tiếp theo sau khi xác thực thành công

		        SecurityContextHolder.getContext().setAuthentication(authentication);
		        CustomerUserDetail customerUserDetail = (CustomerUserDetail) authentication.getPrincipal();
		        String jwt =  tokenProvider.genarateToken(customerUserDetail);
		        List<String> listRoles = customerUserDetail.getAuthorities().stream()
		                                    .map(item -> item.getAuthority())
		                                    .collect(Collectors.toList());

		        return ResponseEntity.ok(new JwtRespone(jwt, customerUserDetail.getUsername(), customerUserDetail.getEmail(), listRoles));
		    } catch (BadCredentialsException e) {
		        // Xử lý khi tên người dùng hoặc mật khẩu không đúng
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password."));
		    } catch (DisabledException e) {
		        // Xử lý khi tài khoản bị vô hiệu hóa
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Account is disabled."));
		    } catch (LockedException e) {
		        // Xử lý khi tài khoản bị khóa
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Account is locked."));
		    } catch (UsernameNotFoundException e) {
		        // Xử lý khi không tìm thấy tên người dùng
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Username not found."));
		    } catch (Exception e) {
		        // Xử lý các trường hợp khác
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error."));
		    }
		
		
	}
	
}
