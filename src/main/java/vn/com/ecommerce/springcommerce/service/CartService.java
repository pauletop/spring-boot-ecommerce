package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.repository.CartRepository;

import java.util.Map;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public void addProductToCart(Account account, Product product, int quantity) {
        Cart cart = account.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setAccount(account);
            account.setCart(cart);
        }
        cart.addToCart(product, quantity);
        cartRepository.save(cart);
    }

    public void removeProductFromCart(Account account, Long productId) {
        Cart cart = account.getCart();
        if (cart == null) {
            return;
        }
        // find product in cart
        for (Product product : cart.getProducts()) {
            if (product.getId().equals(productId)) {
                cart.removeFromCart(product);
                cartRepository.save(cart);
                return;
            }
        }
        cartRepository.save(cart);
    }

    /**
     * Change quantity of product in cart
     * @param account
     * @param productId
     * @param quantity
     * @return delta of total price
     */
    public double changeQuantity(Account account, Long productId, int quantity) {
        Cart cart = account.getCart();
        if (cart == null) {
            return 0;
        }
        double delta = cart.changeQuantity(productId, quantity);
        cartRepository.save(cart);
        return delta;
    }
}
