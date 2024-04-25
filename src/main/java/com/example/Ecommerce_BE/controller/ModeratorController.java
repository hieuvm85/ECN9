package com.example.Ecommerce_BE.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.model.entity.EStatusProduct;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.service.ProductService;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/moderator")
@PreAuthorize("hasRole('MODERATOR')")
public class ModeratorController {

	@Autowired
	private ProductService productService;
	
	@GetMapping("/view/product/waitconfim")
	public ResponseEntity<?> viewAllProductWaitConfirm(){		
		List<Product> products = productService.findByCensorship(EStatusProduct.AWAITING_CENSORSHIP);
		products.sort((p1,p2)-> p1.getDateTimeCreated().compareTo(p2.getDateTimeCreated()));
		return ResponseEntity.ok(products);
	}
	
	
	
	@PutMapping("/confirm")
	public ResponseEntity<?> confirmProduct(@RequestParam("productId") int productId){
		Product product = productService.getById(productId);
		product.setCensorship(EStatusProduct.PASS);
		productService.saveOrUpdate(product);
		
		return ResponseEntity.ok(new MessageResponse("Success: Confirm successfully"));
	}
	@PutMapping("/reject")
	public ResponseEntity<?> rejectProduct(@RequestParam("productId") int productId){
		Product product = productService.getById(productId);
		product.setCensorship(EStatusProduct.REJECT);
		productService.saveOrUpdate(product);
		return ResponseEntity.ok(new MessageResponse("Success: Reject successfully"));
	}
	
	
//	@PutMapping("/recharge")
//	public ResponseEntity<?> rechargeWallet(){
//		return null;
//	}
	
}
