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
    @Autowired
    CartService cartService;

    public Order createOrder(Order newOrder) {
//        cartService.deleteById(newOrder.getId());
        Cart cart = cartService.getCart(newOrder.getId());
        // move items reference from cart to order
        newOrder.setItems(cart.getItems());
        return orderRepository.save(newOrder);
    }
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    public Iterable<Order> getOrderHistory(String email) {
        return orderRepository.findAllByAccountEmail(email);
    }
}
