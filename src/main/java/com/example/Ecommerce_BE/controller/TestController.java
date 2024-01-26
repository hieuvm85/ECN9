package com.example.Ecommerce_BE.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/test")
public class TestController {

	@PostMapping("/all")
	public String all() {
		return "all ok";
	}
	@PostMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String admin() {
		return "admin ok";
	}
}
