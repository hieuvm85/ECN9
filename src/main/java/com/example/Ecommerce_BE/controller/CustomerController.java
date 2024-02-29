package com.example.Ecommerce_BE.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.ERole;
import com.example.Ecommerce_BE.model.entity.Roles;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.RoleService;
import com.example.Ecommerce_BE.model.service.UserService;
import com.example.Ecommerce_BE.payload.request.SignupRequest;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/auth/customer")
public class CustomerController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/test")
	public ResponseEntity<?> getCustomer()
	{
		return ResponseEntity.ok(new Customer());
	}
	@PostMapping("/create")
	public ResponseEntity<?> createCustomer(@RequestBody SignupRequest signupRequest){
		if(userService.existsByUserName(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already"));
		}
		if(userService.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: email is already"));
		}
		
		Customer customer = new Customer();
		customer.setUsername(signupRequest.getUsername());
		customer.setPassword(encoder.encode(signupRequest.getPassword()));
		customer.setEmail(signupRequest.getEmail());
		customer.setName(signupRequest.getName());
		customer.setCreated(LocalDate.now());
		customer.setStatus(true);
		customer.setLinkImageAvatar("https://tse2.mm.bing.net/th?id=OIP.xv5ky4lYh1TkiIZW6wwYJAAAAA&pid=Api&P=0&w=300&h=300");
		
		List<Roles> listRoles = new ArrayList<>();

		Optional<Roles> userRole= roleService.findByRoleName(ERole.ROLE_USER);
		listRoles.add(userRole.get());
		
		
		customer.setListRoles(listRoles);
		customerService.saveOrUpdate(customer);
		return ResponseEntity.ok(new MessageResponse("create account successfuly"));
	}
	@GetMapping("/get")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getCustomer(HttpServletRequest request)
	{
		String strToken = request.getHeader("Authorization");
		if (strToken != null && strToken.startsWith("Bearer ")) {
            // Lấy token từ header
            String token = strToken.substring(7);

            // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
            String username = jwtTokenProvider.getUsernameByJWT(token);   
            return ResponseEntity.ok(customerService.findCustomerByUsername(username));
        }
		else {
			return ResponseEntity.ok(new MessageResponse("bug"));
		}
	}
}
