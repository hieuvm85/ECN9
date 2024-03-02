package com.example.Ecommerce_BE.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
