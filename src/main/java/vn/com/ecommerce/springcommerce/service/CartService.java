package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.domain.SelectedItem;
import vn.com.ecommerce.springcommerce.repository.CartRepository;
import vn.com.ecommerce.springcommerce.repository.SelectedItemRepository;

import java.util.List;

@Service
public class CartService {
    CartRepository cartRepository;
    AccountService accountService;
    ProductService productService;
    SelectedItemService selectedItemService;
   @Autowired
   public CartService(CartRepository cartRepository, AccountService accountService, ProductService productService, SelectedItemService selectedItemService) {
        this.cartRepository = cartRepository;
        this.accountService = accountService;
        this.productService = productService;
        this.selectedItemService = selectedItemService;
    }

    public Cart getCart(String accEmail) {
        Account account = accountService.getAccount(accEmail);
        Cart cart = account.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setAccount(account);
            account.setCart(cart);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    public Cart addProductToCart(Account account, Product product, int quantity) {
        Cart cart = account.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setAccount(account);
            account.setCart(cart);
        }
        cart.addToCart(product, quantity);
        return cartRepository.save(cart);
    }
    public  Cart addProductToCart(String accEmail, Long productId, int qty){
        Account account = accountService.getAccount(accEmail);
        Product product = productService.getProductById(productId);
        return addProductToCart(account,product,qty);
    }

    public Cart removeProductFromCart(Account account, Long productId) {
        Cart cart = account.getCart();
        if (cart == null) {
            return null;
        }
        SelectedItem selectedItem = cart.removeFromCart(productId);
        selectedItemService.deleteById(selectedItem.getId());
        return cartRepository.save(cart);
    }

    public Cart removeProductFromCart(String accEmail, Long productId){
        Account account = accountService.getAccount(accEmail);
        return removeProductFromCart(account,productId);
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
    public double changeQuantity(String accEmail, Long productId, int addQty){
        Account account = accountService.getAccount(accEmail);
        return changeQuantity(account,productId,addQty);
    }

    public SelectedItem getItemFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return null;
        }
        return cart.getFromCart(productId);
    }

    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}
