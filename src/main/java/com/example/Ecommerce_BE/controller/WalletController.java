package com.example.Ecommerce_BE.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.Wallet;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.WalletService;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/wallet")
@PreAuthorize("hasRole('USER')")
public class WalletController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private WalletService walletService;
	
	
	@GetMapping("/checkPay")
	public ResponseEntity<?> checkPay(HttpServletRequest request,@RequestParam("amount") int amount ){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        if(customer.getWallet().getBalance()>= amount)
        	return ResponseEntity.ok(true);
        else
        	return ResponseEntity.ok(false);
	}
	@PutMapping("/topUpMoney")
	public ResponseEntity<?> topUpMoney(HttpServletRequest request,@RequestParam("amount") int amount){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        Wallet wallet =customer.getWallet();
        wallet.setBalance(wallet.getBalance()+amount);
        walletService.saveOrUpdate(wallet);
        return ResponseEntity.ok(new MessageResponse("Success: Top up money success"));
	}
}
