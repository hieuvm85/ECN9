package com.example.Ecommerce_BE.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Cart;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.FeedBack;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.service.AddressService;
import com.example.Ecommerce_BE.model.service.CartService;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.FeedbackService;
import com.example.Ecommerce_BE.model.service.NotificationService;
import com.example.Ecommerce_BE.model.service.OrderService;
import com.example.Ecommerce_BE.model.service.ProductService;
import com.example.Ecommerce_BE.model.service.ShopService;
import com.example.Ecommerce_BE.payload.request.FeedbackRequest;
import com.example.Ecommerce_BE.payload.response.CartResponse;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/feedback")
@PreAuthorize("hasRole('USER')")
public class FeedbackController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private ProductService productService;
	@Autowired
	private CartService cartService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private AddressService  addressService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private FeedbackService feedbackService;
	
	@GetMapping("/get/cartToFeedback")
	public ResponseEntity<?> getCartToFeedback(HttpServletRequest  request){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        List<Cart> carts= cartService.getCartToFeedback(customer.getId());
        List<CartResponse> cartResponses= new ArrayList<>();
        for(Cart cart:carts) {
        	cartResponses.add(new CartResponse(cart));
        }
        
        return ResponseEntity.ok(cartResponses);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addFeedback (HttpServletRequest  request,@RequestBody FeedbackRequest feedbackRequest){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        if(feedbackRequest.getCartResponse()==null)
        	return ResponseEntity.ok(new MessageResponse("Error: You have not purchased yet"));
        FeedBack feedBack= new FeedBack();
        feedBack.setCreated(LocalDateTime.now());
        feedBack.setComment(feedbackRequest.getComment());
        feedBack.setCart(cartService.getById(feedbackRequest.getCartResponse().getId()));
        feedBack.setProduct(productService.getById(feedbackRequest.getCartResponse().getProduct().getId()));
        feedBack.setStar(feedbackRequest.getStar());
        feedBack.setLinkImage(feedbackRequest.getLinkImage());
        feedBack.setCustomer(customer);
        feedbackService.saveOrUpdate(feedBack);
        Product product = productService.getById(feedBack.getCart().getProduct().getId());
        List<FeedBack> feedBacks = product.getFeedBacks();
        int sum=0;
        int num=0;
        for(FeedBack fb:feedBacks) {
        	num+=1;
        	sum+=fb.getStar();
        }
        product.setRate((float)sum/num);
        productService.saveOrUpdate(product);
		return ResponseEntity.ok(new MessageResponse("Success: Feedback Successfully"));
	}
	
}
