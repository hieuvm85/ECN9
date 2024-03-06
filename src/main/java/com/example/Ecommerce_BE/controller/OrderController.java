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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;

import com.example.Ecommerce_BE.jwt.JwtTokenProvider;
import com.example.Ecommerce_BE.model.entity.Address;
import com.example.Ecommerce_BE.model.entity.Cart;
import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.entity.EPaymentOption;
import com.example.Ecommerce_BE.model.entity.EStatusOrder;
import com.example.Ecommerce_BE.model.entity.EStatusProduct;
import com.example.Ecommerce_BE.model.entity.Notification;
import com.example.Ecommerce_BE.model.entity.Order;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.entity.Wallet;
import com.example.Ecommerce_BE.model.service.AddressService;
import com.example.Ecommerce_BE.model.service.CartService;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.model.service.NotificationService;
import com.example.Ecommerce_BE.model.service.OrderService;
import com.example.Ecommerce_BE.model.service.ProductService;
import com.example.Ecommerce_BE.model.service.ShopService;
import com.example.Ecommerce_BE.model.service.WalletService;
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
	@Autowired
	private WalletService walletService;
	
	
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
        		
        		if(cartResponse.getId()==0)
        		{
        			Cart cart= new Cart();
        	        cart.setQuantity(cartResponse.getQuantity());
        	        cart.setStatusBought(false);
        	        cart.setDateTimeCreated(LocalDateTime.now());	
        	        cart.setCustomer(customer);      
        	        cart.setProduct(productService.getById(cartResponse.getProduct().getId()));
        	        cartService.saveOrUpdate(cart);
        	        carts.add(cart);
        		}
        		else
        		{
        			Cart cart= cartService.getById(cartResponse.getId());
        			carts.add(cart);
        		}
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
	
	
	@PutMapping("/confirm")
	public ResponseEntity<?> confirmOrder(HttpServletRequest request,@RequestParam("orderId") int orderId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Order order = orderService.getById(orderId);
        if(order.getShop().getCustomer().getId()==customer.getId() & order.getStatusOrder()==EStatusOrder.WAITE_CONFIRM) {
        	order.setStatusOrder(EStatusOrder.PREPARE_GOODS);
        	orderService.saveOrUpdate(order);
        	Wallet wallet = customer.getWallet();
        	int tax= (int) order.getTotalMoneyItem()*2/100;
        	wallet.setBalance(wallet.getBalance()-  tax);
        	walletService.saveOrUpdate(wallet);
        	notificationService.saveOrUpdate(new Notification("Ví","Phí dịch vụ đơn hàng: -"+tax+", mã đơn hàng: "+orderId,customer ));
        	return ResponseEntity.ok(new MessageResponse("Success: CONFIRM successfully"));
        }
        else
        	return ResponseEntity.ok(new MessageResponse("Error: CONFIRM fail"));        
	}
	@PutMapping("/cancel")
	public ResponseEntity<?> cancelled(HttpServletRequest request,@RequestParam("orderId") int orderId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Order order = orderService.getById(orderId);
        
        
        if((customer.getId()==order.getCustomer().getId() || order.getShop().getCustomer().getId()==customer.getId()) 
        		&(order.getStatusOrder()==EStatusOrder.WAITE_CONFIRM || order.getStatusOrder()==EStatusOrder.PREPARE_GOODS)) {
        	order.setStatusOrder(EStatusOrder.CANCELLED);
        	orderService.saveOrUpdate(order);
        	return ResponseEntity.ok(new MessageResponse("Success: CANCEL successfully"));
        }
        else
        	return ResponseEntity.ok(new MessageResponse("Error: CANCEL fail"));
        
	}
	@PutMapping("/prepare")
	public ResponseEntity<?> prepareOrder(HttpServletRequest request,@RequestParam("orderId") int orderId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Order order = orderService.getById(orderId);
        if(order.getShop().getCustomer().getId()==customer.getId() & order.getStatusOrder()==EStatusOrder.PREPARE_GOODS) {
        	order.setStatusOrder(EStatusOrder.DELIVERING);
        	orderService.saveOrUpdate(order);
        	return ResponseEntity.ok(new MessageResponse("Success: PREPARE successfully"));
        }
        else
        	return ResponseEntity.ok(new MessageResponse("Error: PREPARE fail"));       
	}
	@PutMapping("/delevered")
	public ResponseEntity<?> deleveredOrder(HttpServletRequest request,@RequestParam("orderId") int orderId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Order order = orderService.getById(orderId);
        if( order.getStatusOrder()==EStatusOrder.DELIVERING) {
        	order.setStatusOrder(EStatusOrder.DELEVERED);
        	orderService.saveOrUpdate(order);
        	if(true) {
        		Wallet wallet= order.getShop().getCustomer().getWallet();
        		wallet.setBalance(wallet.getBalance()+ order.getTotalMoneyItem());
        		walletService.saveOrUpdate(wallet);
        		notificationService.saveOrUpdate(new Notification("Ví",
        				"Thanh toán tiền đơn hàng: +"+order.getTotalMoneyItem()+", mã đơn hàng: "+ order.getId(),order.getShop().getCustomer()));
        		notificationService.saveOrUpdate(new Notification("Đơn hàng",
        				"Giao hàng thành công, mã đơn hàng: "+ order.getId(),order.getShop().getCustomer()));
        		notificationService.saveOrUpdate(new Notification("Đơn hàng",
        				"Giao hàng thành công, mã đơn hàng: "+ order.getId(),order.getCustomer()));
        	}
        	return ResponseEntity.ok(new MessageResponse("Success: DELEVERED successfully"));
        }
        else
        	return ResponseEntity.ok(new MessageResponse("Error: DELEVERED fail"));       
	}
	
	@PutMapping("/return")
	public ResponseEntity<?> returnOrder(HttpServletRequest request,@RequestParam("orderId") int orderId){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        
        Order order = orderService.getById(orderId);
        if(customer.getId()==order.getCustomer().getId() & order.getStatusOrder()==EStatusOrder.DELIVERING) 
        {
        	order.setStatusOrder(EStatusOrder.RETURN_GOODS);
        	order.setStatusOrder(EStatusOrder.DELIVERING);
        	orderService.saveOrUpdate(order);
        	return ResponseEntity.ok(new MessageResponse("Success: RETURN_GOODS successfully"));
        }
        else
        	return ResponseEntity.ok(new MessageResponse("Error: RETURN_GOODS fail"));       
	}
	
	@GetMapping("/shop/view/order/status")
	public ResponseEntity<?> shopViewOrderConfirm(HttpServletRequest request,@RequestParam("statusOrder") EStatusOrder statusOrder){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        return ResponseEntity.ok(orderService.getByShopAndStatusOrder(customer.getShop().getId(),statusOrder));
	}
	@GetMapping("/customer/view/order/status")
	public ResponseEntity<?> customerViewOrderConfirm(HttpServletRequest request,@RequestParam("statusOrder") EStatusOrder statusOrder){
		String strToken = request.getHeader("Authorization");
        String token = strToken.substring(7);
        // Sử dụng phương thức để lấy username từ token (giả sử bạn đã có JwtTokenUtil)
        String username = jwtTokenProvider.getUsernameByJWT(token);   
        Customer customer = customerService.findCustomerByUsername(username);
        return ResponseEntity.ok(orderService.getByCustomerAndStatusOrder(customer.getId(),statusOrder));
	}
	
	@GetMapping("/previewNow")
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
        Cart cartx = cartService.checkCartContain(customer.getId(), productId);
	        cartx=new Cart();
	        cartx.setQuantity(quantity);
	        cartx.setStatusBought(false);
	        cartx.setDateTimeCreated(LocalDateTime.now());	
	        cartx.setCustomer(customer);      
	        cartx.setProduct(product);	
	        
	        CartResponse cart = new CartResponse(cartx);
	        
	        List<OrderPreviewResponse> orders = new ArrayList<>();
	        OrderPreviewResponse order = new OrderPreviewResponse();
	            // set cac thuoc tinh cho tung order
	            
	            int totalMoneyItem = 0;
		          	Product product1 = productService.getById(cart.getProduct().getId());
		          	if(product1.getQuantity()>= cart.getQuantity())
		          		totalMoneyItem += cart.getQuantity()*cart.getProduct().getSellingPrice();
		          	else
		          	{
		          		return ResponseEntity.ok(new ConsumesRequestCondition("The product"+product1.getTitle()+"is out of stock"));
		          	}
	            
	            order.setAddressDelivery(customer.getAdress());
	            order.setTotalMoneyItem(totalMoneyItem);
	            order.setTotalMonneyShip(50000);
	            order.setAmount(totalMoneyItem+50000);
	            order.setPaymentOption(EPaymentOption.PAY_CASH);
	            
	            List<CartResponse> carts = new ArrayList<>();
	            carts.add(cart);
	            order.setCarts(carts);
	            order.setAddressDelivery(customer.getAdress());
	            orders.add(order);
	        

        return ResponseEntity.ok(orders);
	}
	
}
