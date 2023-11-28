package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Order;
import vn.com.ecommerce.springcommerce.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void createOrder(Cart cart, String address) {
        Order newOrder = cart.moveCartToOrder();
        newOrder.setAddress(address);
        orderRepository.save(newOrder);
    }
}
