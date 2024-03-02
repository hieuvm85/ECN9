package com.example.Ecommerce_BE.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.EStatusProduct;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.entity.ProductDescriptionDetail;
import com.example.Ecommerce_BE.model.entity.Shop;
import com.example.Ecommerce_BE.model.service.AddressService;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.ProductDescriptionDetailService;
import com.example.Ecommerce_BE.model.service.ProductService;
import com.example.Ecommerce_BE.model.service.ShopService;
import com.example.Ecommerce_BE.payload.request.CreateProductRequest;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/product")
@PreAuthorize("hasRole('USER')")
public class ProductController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductDescriptionDetailService productDescriptionDetailService;
	@Autowired
	private ProductService productService;
	
	@PostMapping("/create")
	public ResponseEntity<?> addProduct(HttpServletRequest request,@RequestBody CreateProductRequest productRequest){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        // check shop exists
        Shop shop = customer.getShop();
        
        Product product= new Product();
        product.setTitle(productRequest.getTitle());
        product.setQuantity(productRequest.getQuantity());
        product.setWeight(productRequest.getWeight());
        product.setDescription(productRequest.getDescription());
        product.setLinkImages(productRequest.getLinkImages());
        
        product.setStatusSale(false);
        product.setCensorship(EStatusProduct.AWAITING_CENSORSHIP);
        product.setRate(5.0f);
        product.setShop(shop);
        product.setQuantitySold(0);
        product.setDateTimeCreated(LocalDateTime.now());
        
        List<ProductDescriptionDetail> productDescriptionDetails = new ArrayList<>();       
        for(CreateProductRequest.ProductDetail productDetail: productRequest.getProductDescriptionDetails())
        {
        	ProductDescriptionDetail productDescriptionDetail =  new ProductDescriptionDetail();
        	productDescriptionDetail.setTitle(productDetail.getTitle());
        	productDescriptionDetail.setDescription(productDetail.getDescription());
        	
        	productDescriptionDetailService.saveOrUpdate(productDescriptionDetail);
        	
        	productDescriptionDetails.add(productDescriptionDetail);
        }
        product.setProductDescriptionDetails(productDescriptionDetails);
        
        productService.saveOrUpdate(product);
        return ResponseEntity.ok(product);
	}
	
	@GetMapping("/view/myshop")
	public ResponseEntity<?> viewMyShop(HttpServletRequest request){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        // check shop exists
        
        return ResponseEntity.ok(customer.getShop().getProducts());
	}
	
	@PutMapping("/set/onsale")
	public ResponseEntity<?> setOnsale(HttpServletRequest request,@RequestParam("productId") int productId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Product product = productService.getById(productId);
        product.setStatusSale(true);
        
        if(product.getShop().getId() != customer.getShop().getId())       
        	return ResponseEntity.ok(new MessageResponse("you have no right to change"));
        else if(customer.getWallet().getBalance() <= 50000)
        	return ResponseEntity.ok(new MessageResponse("You do not have enough money for the service fee"));
        else if(product.getQuantity()<=0)
        	return ResponseEntity.ok(new MessageResponse("Quantity is not enough"));     	
        else
        {
        	productService.saveOrUpdate(product);
    		return ResponseEntity.ok(new MessageResponse("Set on sale successfully"));
        }
	}
	
	@PutMapping("/set/offsale")
	public ResponseEntity<?> setOffsale(HttpServletRequest request,@RequestParam("productId") int productId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Product product = productService.getById(productId);
        product.setStatusSale(false);
        
        if(product.getShop().getId() == customer.getShop().getId())
        {
        	productService.saveOrUpdate(product);
        	return ResponseEntity.ok(new MessageResponse("Set off sale successfully"));
        }
        else
        {
        	return ResponseEntity.ok(new MessageResponse("Set off sale fail"));
        }
	}
	
	@PutMapping("/set/quantity")
	public ResponseEntity<?> setQuantity(HttpServletRequest request,@RequestParam("productId") int productId,@RequestParam("quantity") int quantity){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Product product = productService.getById(productId);
        if(quantity > 0)
        	product.setQuantity(quantity);
        else if(quantity==0)
        {
        	product.setQuantity(0);
        	product.setStatusSale(false);
        }
        else
        	return ResponseEntity.ok(new MessageResponse("quantity invalid"));
        
        if(product.getShop().getId() == customer.getShop().getId())
        {
        	productService.saveOrUpdate(product);
        	return ResponseEntity.ok(new MessageResponse("Set quantity successfully"));
        }
        else
        {
        	return ResponseEntity.ok(new MessageResponse("Set quantity fail"));
        }
	}
}
