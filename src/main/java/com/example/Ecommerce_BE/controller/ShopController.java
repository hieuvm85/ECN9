package com.example.Ecommerce_BE.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Address;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.EStatusShop;
import com.example.Ecommerce_BE.model.entity.Shop;
import com.example.Ecommerce_BE.model.service.AddressService;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.RoleService;
import com.example.Ecommerce_BE.model.service.ShopService;
import com.example.Ecommerce_BE.model.service.UserService;
import com.example.Ecommerce_BE.payload.request.CreateShopRequest;
import com.example.Ecommerce_BE.payload.request.UpdateShopRequest;
import com.example.Ecommerce_BE.payload.response.MessageResponse;
import com.example.Ecommerce_BE.repository.CustomerRepository;

@RestController
@RequestMapping("/api/shop")
@PreAuthorize("hasRole('USER')")
public class ShopController {
//	@Autowired
//	private RoleService roleService;
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private PasswordEncoder encoder;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ShopService shopService;
	
	
	@PostMapping("/create")
	public ResponseEntity<?> createShop(HttpServletRequest request, @RequestBody CreateShopRequest shopRequest){
		String strToken = request.getHeader("Authorization");
		if (strToken != null && strToken.startsWith("Bearer ")) {
            // Lấy token từ header
            String token = strToken.substring(7);

            // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
            String username = jwtTokenProvider.getUsernameByJWT(token);   

            Customer customer = customerService.findCustomerByUsername(username);

            // check shop exists
            if(customer.getShop()==null)
            {
        		Shop shop =new Shop();
            	// xu li tao shop
                if(shopService.existsByNameShop(shopRequest.getNameShop())) {                	
					return ResponseEntity.ok(new MessageResponse("nameShop already exists"));
				}
                shop.setNameShop(shopRequest.getNameShop());
                Address address = addressService.validAddress(shopRequest.getAddressShop());
                shop.setAddressShop(address);
                shop.setLinkImageAvatarShop("https://tse2.mm.bing.net/th?id=OIP.xv5ky4lYh1TkiIZW6wwYJAAAAA&pid=Api&P=0&w=300&h=300");
                shop.setLinkImageShop("https://tse2.mm.bing.net/th?id=OIP.xv5ky4lYh1TkiIZW6wwYJAAAAA&pid=Api&P=0&w=300&h=300");
                shop.setProducts(new ArrayList<>());
                shop.setCustomer(customer);
                shop.setStatus(EStatusShop.ON_SALE);
                shopService.saveOrUpdate(shop);
                return ResponseEntity.ok(shop);
            } 
            else {
				return ResponseEntity.ok(new MessageResponse("Error: Shop already exists"));
			}   
        }
		else {
			return ResponseEntity.ok(new MessageResponse("Error: Bug Auth"));
		}
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<?> updateShop(HttpServletRequest request, @RequestBody UpdateShopRequest shopRequest){
		String strToken = request.getHeader("Authorization");
		if (strToken != null && strToken.startsWith("Bearer ")) {
            // Lấy token từ header
            String token = strToken.substring(7);

            // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
            String username = jwtTokenProvider.getUsernameByJWT(token);   

            Customer customer = customerService.findCustomerByUsername(username);
            // check shop exists
           Shop shop = customer.getShop();
           shop.setLinkImageAvatarShop(shopRequest.getLinkImageAvatarShop());
           shop.setLinkImageShop(shopRequest.getLinkImageShop());
           shopService.saveOrUpdate(shop);
            return ResponseEntity.ok(shop); 
        }
		else {
			return ResponseEntity.ok(new MessageResponse("Error: Bug Auth"));
		}
	}
}
