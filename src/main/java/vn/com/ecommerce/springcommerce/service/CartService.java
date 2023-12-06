package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.domain.SelectedItem;
import vn.com.ecommerce.springcommerce.repository.CartRepository;

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

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    /**
     * Change quantity of product in cart
     * @param account is owner of cart
     * @param productId is id of product
     * @param quantity is new quantity of product
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
