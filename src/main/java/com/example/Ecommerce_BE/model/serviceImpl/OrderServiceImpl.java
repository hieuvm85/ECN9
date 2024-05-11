package com.example.Ecommerce_BE.model.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Ecommerce_BE.model.entity.Cart;
import com.example.Ecommerce_BE.model.entity.EPaymentOption;
import com.example.Ecommerce_BE.model.entity.EStatusOrder;
import com.example.Ecommerce_BE.model.entity.Notification;
import com.example.Ecommerce_BE.model.entity.Order;
import com.example.Ecommerce_BE.model.entity.Product;
import com.example.Ecommerce_BE.model.entity.Wallet;
import com.example.Ecommerce_BE.model.service.OrderService;
import com.example.Ecommerce_BE.repository.CartRepository;
import com.example.Ecommerce_BE.repository.NotificationRepository;
import com.example.Ecommerce_BE.repository.OrderRepository;
import com.example.Ecommerce_BE.repository.ProductRepository;
import com.example.Ecommerce_BE.repository.WalletRepository;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	@Autowired 
	private ProductRepository productRepository;
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private CartRepository cartRepository;
	
	@Override
	public Order saveOrUpdate(Order order) {
		// TODO Auto-generated method stub
		return orderRepository.save(order);
	}
	
	
	@Override
	@Transactional
	public boolean orderAll(List<Order> orders) {
		// TODO Auto-generated method stub
		try {
			orderRepository.saveAll(orders);
			int totalMoney=0;
			List<Notification> notifications = new ArrayList<>();
			for(Order order:orders) {
				totalMoney+=order.getAmount();
				for(Cart cart:order.getCarts()) {
					Product product = cart.getProduct();
					if(product.getQuantity()<cart.getQuantity())
						throw new RuntimeException(product.getTitle()+"không đủ số lượng sản phẩm");
					else
					{
						int quantity =product.getQuantity()-cart.getQuantity();
						product.setQuantity(quantity);
						quantity= product.getQuantitySold()+cart.getQuantity();
						product.setQuantitySold(quantity);
						productRepository.save(product);
					}
					cart.setStatusBought(true);
					cart.setOrder(order);
					cartRepository.save(cart);
				}
				// thong bao cho nguoi dung
				notifications.add(new Notification("Đơn hàng","Đơn hàng  đã đặt thành công, mã đơn hàng: "+ order.getId(),order.getCustomer()));
				notifications.add(new Notification("Shop","Bạn có đơn đặt hàng mới, mã đơn hàng :"+ order.getId(),order.getShop().getCustomer()));			
			}
			if(orders.get(0).getPaymentOption()==EPaymentOption.PAY_WALLET)
			{
				Wallet wallet= orders.get(0).getCustomer().getWallet();
				int balance = wallet.getBalance() - totalMoney;
				if(balance<0)
					throw new RuntimeException("Không đủ số dư");
				wallet.setBalance(balance);
				walletRepository.save(wallet);
				notifications.add(new Notification("ví","Thanh toán đơn hàng thành công -"+ totalMoney+ "số dư ví: "+ balance,wallet.getCustomer()));
			}
			notificationRepository.saveAll(notifications);
						
		} catch (Exception e) {
			return false;
		}
		return true;	
	}


	@Override
	public Order getById(int id) {
		// TODO Auto-generated method stub
		return orderRepository.getById(id);
	}



	@Override
	public List<Order> getByCustomerAndStatusOrder(int idCus, EStatusOrder eStatusOrder) {
		// TODO Auto-generated method stub
		return orderRepository.findByCustomerIdAndStatusOrder(idCus, eStatusOrder);
	}


	@Override
	public List<Order> getByShopAndStatusOrder(int idShop, EStatusOrder eStatusOrder) {
		// TODO Auto-generated method stub
		return orderRepository.findByShopIdAndStatusOrder(idShop, eStatusOrder);
	}


	@Override
	public List<Order> getByEstatusOrderAndCreatedBetween(EStatusOrder eStatusOrder, LocalDateTime startDate,
			LocalDateTime endDate) {
		// TODO Auto-generated method stub
		return orderRepository.findByStatusOrderAndCreatedBetween(eStatusOrder, startDate, endDate);
	}


	@Override
	public List<Order> getByCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
		// TODO Auto-generated method stub
		return orderRepository.findByCreatedBetween( startDate, endDate);
	}


	@Override
	public int countByCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
		// TODO Auto-generated method stub
		return orderRepository.countByCreatedBetween(startDate,endDate);
	}

}
