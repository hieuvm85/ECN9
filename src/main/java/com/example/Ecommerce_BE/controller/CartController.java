package com.example.Ecommerce_BE.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Cart;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.EStatusProduct;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.service.AddressService;
import com.example.Ecommerce_BE.model.service.CartService;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.ProductDescriptionDetailService;
import com.example.Ecommerce_BE.model.service.ProductService;
import com.example.Ecommerce_BE.model.service.ShopService;
import com.example.Ecommerce_BE.payload.response.CartResponse;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private ProductService productService;
	@Autowired
	private CartService cartService;
	
	

	@PostMapping("/add")
	public ResponseEntity<?> addCart(HttpServletRequest request,@RequestParam("productId") int productId,@RequestParam("quantity") int quantity )
	{
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        Product product = productService.getById(productId);
        // check khong duoc dat hang cua chinh minh
        if(!product.isStatusSale() || product.getCensorship()!=EStatusProduct.PASS 
        		|| product.getShop().getCustomer().getId()==customer.getId())
        	return ResponseEntity.ok(new MessageResponse("Error: Add to cart fail"));
        // kiem tra cart da ton tai hay chua neu co thi cap nhat them so luong
        Cart cart = cartService.checkCartContain(customer.getId(), productId);
        if(cart!=null)
        {
        	cart.setQuantity(quantity+ cart.getQuantity());
        	cart.setDateTimeCreated(LocalDateTime.now());
        }
        else
        {
	        cart=new Cart();
	        cart.setQuantity(quantity);
	        cart.setStatusBought(false);
	        cart.setDateTimeCreated(LocalDateTime.now());	
	        cart.setCustomer(customer);      
	        cart.setProduct(product);	        
        }
        cartService.saveOrUpdate(cart);
        return ResponseEntity.ok(new MessageResponse("Success: Add to cart successfully"));
	}

	@GetMapping("/viewMyCart")
	public ResponseEntity<?> viewMyCart(HttpServletRequest request){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        List<Cart> carts= cartService.viewMyCart(customer.getId());
        List<CartResponse> cartResponses= new ArrayList<>();
        for(Cart cart:carts)
        {
        	CartResponse cartResponse = new CartResponse(cart);
        	cartResponses.add(cartResponse);
        }
        cartResponses.sort((p1,p2)-> p2.getDateTimeCreated().compareTo(p1.getDateTimeCreated()));
        return ResponseEntity.ok(cartResponses);
        
	}
	@PutMapping("/set/quantity")
	public ResponseEntity<?> quantityCart(HttpServletRequest request,@RequestParam("cartId") int cartId,@RequestParam("quantity") int quantity)
	{
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        Cart cart = cartService.getById(cartId);
        
        if(cart.getCustomer().getId()== customer.getId()) {
        	cart.setQuantity(quantity);
        	return ResponseEntity.ok(new CartResponse(cartService.saveOrUpdate(cart)) );
        }
        else
        	return ResponseEntity.ok(new MessageResponse("Error: Set quantity fail"));
	}
	
	
	

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCart(HttpServletRequest request,@RequestParam("cartId") int cartId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        Cart cart = cartService.getById(cartId);
        if(cart.getCustomer().getId()== customer.getId()) {
        	cartService.deleteCart(cartId);
        	return ResponseEntity.ok(new MessageResponse("Success: Delete successfull"));
        }
        else
        	return ResponseEntity.ok(new MessageResponse("Error: Delete fail"));
	}
}
