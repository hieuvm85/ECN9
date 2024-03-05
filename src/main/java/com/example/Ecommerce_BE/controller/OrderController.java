package com.example.Ecommerce_BE.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Address;
import com.example.Ecommerce_BE.model.entity.Cart;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.EPaymentOption;
import com.example.Ecommerce_BE.model.entity.EStatusOrder;
import com.example.Ecommerce_BE.model.entity.Order;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.service.AddressService;
import com.example.Ecommerce_BE.model.service.CartService;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.NotificationService;
import com.example.Ecommerce_BE.model.service.OrderService;
import com.example.Ecommerce_BE.model.service.ProductService;
import com.example.Ecommerce_BE.model.service.ShopService;
import com.example.Ecommerce_BE.payload.response.CartResponse;
import com.example.Ecommerce_BE.payload.response.MessageResponse;
import com.example.Ecommerce_BE.payload.response.OrderPreviewResponse;

@RestController
@RequestMapping("/api/order")
@PreAuthorize("hasRole('USER')")
public class OrderController {
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
	
	
	
	@PostMapping("/preview")
	public ResponseEntity<?> orderPreview(HttpServletRequest request,@RequestBody List<CartResponse> cartResponses){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        
        // Nhóm các cart theo id của cửa hàng
        Map<Integer, List<CartResponse>> shopToCartsMap = new HashMap<>();
        for(CartResponse cartResponse:cartResponses) {
        	int shopId = cartResponse.getProduct().getShop().getId();
        	List<CartResponse> carts = shopToCartsMap.getOrDefault(shopId, new ArrayList<>());
        	carts.add(cartResponse);
        	shopToCartsMap.put(shopId, carts);
        }
        
        
        
//        
//        
//   
//        List<Order> orders = new ArrayList<>();
//        for (Map.Entry<Integer, List<Cart>> entry : shopToCartsMap.entrySet()) {
//            int shopId = entry.getKey();
//            List<Cart> carts = entry.getValue();
//            Order order = new Order();
//            // set cac thuoc tinh cho tung order
//            order.setCreated(LocalDateTime.now());
//            
//            int totalMoneyItem = 0;
//            for(Cart cart :carts) {
//            	totalMoneyItem += cart.getQuantity()*cart.getProduct().getSellingPrice();
//            }
//            
//            order.setTotalMoneyItem(totalMoneyItem);
//            order.setTotalMonneyShip(50000);
//            order.setAmount(totalMoneyItem+50000);
//            order.setPaymentOption(EPaymentOption.PAY_CASH);
//            order.setStatusOrder(EStatusOrder.WAITE_CONFIRM);
//            order.setCarts(carts);
//            order.setAddressDelivery(customer.getAdress());
//            order.setCustomer(customer);
//            order.setShop(customer.getShop());
//            
//            
//            orders.add(order);
//        }
        
        
       List<OrderPreviewResponse> orders = new ArrayList<>();
      for (Map.Entry<Integer, List<CartResponse>> entry : shopToCartsMap.entrySet()) {
          int shopId = entry.getKey();
          List<CartResponse> carts = entry.getValue();
          OrderPreviewResponse order = new OrderPreviewResponse();
          // set cac thuoc tinh cho tung order
          
          int totalMoneyItem = 0;
          for(CartResponse cart :carts) {
        	Product product = productService.getById(cart.getProduct().getId());
        	if(product.getQuantity()>= cart.getQuantity())
        		totalMoneyItem += cart.getQuantity()*cart.getProduct().getSellingPrice();
        	else
        	{
        		return ResponseEntity.ok(new ConsumesRequestCondition("The product"+product.getTitle()+"is out of stock"));
        	}
          }
          order.setAddressDelivery(customer.getAdress());
          order.setTotalMoneyItem(totalMoneyItem);
          order.setTotalMonneyShip(50000);
          order.setAmount(totalMoneyItem+50000);
          order.setPaymentOption(EPaymentOption.PAY_CASH);
          order.setCarts(carts);
          order.setAddressDelivery(customer.getAdress());
          orders.add(order);
      }
		
		return ResponseEntity.ok(orders);
	}
	
	@PostMapping("/order")
	public ResponseEntity<?> order(HttpServletRequest request,@RequestBody List<OrderPreviewResponse> orderPreviewResponses){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        if(orderPreviewResponses.size()==0)
        	return ResponseEntity.ok(new MessageResponse("Error: Order empty"));
        List<Order> orders = new ArrayList<>();
        
        for(OrderPreviewResponse orderPreviewResponse: orderPreviewResponses) {
        	Order order = new Order();
        	
        	//set thuoc tinh
        	order.setCreated(LocalDateTime.now());
        	order.setTotalMoneyItem(orderPreviewResponse.getTotalMoneyItem());
        	order.setTotalMonneyShip(orderPreviewResponse.getTotalMonneyShip());
        	order.setAmount(orderPreviewResponse.getAmount());
        	order.setMessage(orderPreviewResponse.getMessage());
        	order.setPaymentOption(orderPreviewResponse.getPaymentOption());
        	order.setStatusOrder(EStatusOrder.WAITE_CONFIRM);
        	List<Cart> carts = new ArrayList<>();
        	for(CartResponse cartResponse:   orderPreviewResponse.getCarts()) {
        		Cart cart= cartService.getById(cartResponse.getId());
        		carts.add(cart);
        	}
        	order.setCarts(carts);
        	
        	Address address = orderPreviewResponse.getAddressDelivery();
        	if(address == null)
        		return ResponseEntity.ok(new MessageResponse("Error: Address invalid"));
        	order.setAddressDelivery(addressService.validAddress(address));
        	order.setCustomer(customer);
        	order.setShop(carts.get(0).getProduct().getShop());
        	
        	
        	orders.add(order);
        }
        if(orderService.orderAll(orders))
        	return ResponseEntity.ok(new MessageResponse("Success: Order successfully"));
        else
        	return ResponseEntity.ok(new MessageResponse("Error: Order fail"));       
	}
}
