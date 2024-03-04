package com.example.Ecommerce_BE.controller;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.Notification;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.NotificationService;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/notification")
@PreAuthorize("hasRole('USER')")
public class NotificationController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private NotificationService notificationService;
	
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllNotification(HttpServletRequest request){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        List<Notification> notifications=customer.getNotifications();
        notifications.sort(Comparator.comparingInt(Notification::getId).reversed());
        return ResponseEntity.ok(notifications);    
	}
	@PutMapping("/read")
	public ResponseEntity<?> setRead(@RequestParam("notificationId") int notificationId){
		Notification notification =notificationService.getById(notificationId);
		if(notification.isStatus())
			return ResponseEntity.ok(notification);
		notification.setStatus(true);
		notificationService.saveOrUpdate(notification);
		return ResponseEntity.ok(notification);
	}
	@PutMapping("/reads")
	public ResponseEntity<?> setReads(@RequestParam("notificationIds") int[] notificationIds){
		for(int notificationId:notificationIds)
		{	
			Notification notification =notificationService.getById(notificationId);
			if(!notification.isStatus())
			{	
				notification.setStatus(true);
				notificationService.saveOrUpdate(notification);
			}
		}
		return ResponseEntity.ok(new MessageResponse("set read notifications successfully"));
	}
	
}
