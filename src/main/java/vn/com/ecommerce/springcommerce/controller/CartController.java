package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.ecommerce.springcommerce.DTO.ResponseMessage;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.service.AccountService;
import vn.com.ecommerce.springcommerce.service.CartService;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    final CartService cartService;
    final AccountService accountService;
    @Autowired
    public CartController(CartService cartService, AccountService accountService) {
        this.cartService = cartService;
        this.accountService = accountService;
    }
    @GetMapping({"/", ""})
    String index(@Nullable @SessionAttribute(value = "accEmail", required = false) String email,
                 @SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                 @Nullable @SessionAttribute(value = "sCart", required = false) Cart sCart,
                 Model model, HttpServletRequest request){
        if (email == null) {
            return "redirect:/account/login";
        }
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
        }
        sCart = cartService.getCart(email);
        request.getSession().setAttribute("sCart", sCart);
        model.addAttribute("sCart", sCart);
        model.addAttribute("navActive", "cart");
        return "cart";
    }
    @GetMapping("/checkout")
    String checkout(@Nullable @SessionAttribute(value = "accEmail", required = false) String email,
                    @Nullable @SessionAttribute(value = "sCart", required = false) Cart sCart,
                    @Nullable @SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                    Model model){
        if (email == null) {
            return "redirect:/account/login";
        }
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
        }
        Account account = accountService.getAccount(email);
        Cart cart = account.getCart();
        model.addAttribute("sCart", sCart);
        model.addAttribute("cart", cart);
        model.addAttribute("user", account);
        model.addAttribute("navActive", "cart");
        return "checkout";
    }

    @PostMapping("/add")
    ResponseEntity<ResponseMessage<String>> add(@RequestBody Map<String, Object> body, @SessionAttribute(value = "accEmail", required = false) String email,
                                                HttpServletRequest request){
        if (email == null) {
            ResponseMessage<String> responseMessage = new ResponseMessage<>(Response.SC_UNAUTHORIZED, "Please login to add product to cart");
            return ResponseEntity.ok(responseMessage);
        }
        long productId = decodeBase64ToLong((String) body.get("pdId"));
        int qty = Integer.parseInt(body.get("qty").toString());
        request.getSession().setAttribute("sCart", cartService.addProductToCart(email, productId, qty));
        ResponseMessage<String> responseMessage = new ResponseMessage<>(Response.SC_OK, "Add product to cart successfully");
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/remove")
    ResponseEntity<ResponseMessage<String>> remove(@RequestBody Map<String, Object> body,
                                                          @SessionAttribute(value = "accEmail", required = false) String email,
                                                   HttpServletRequest request){
        long productId = decodeBase64ToLong((String) body.get("pdId"));
        request.getSession().setAttribute("sCart", cartService.removeProductFromCart(email, productId));
        ResponseMessage<String> responseMessage = new ResponseMessage<>(Response.SC_OK, "Remove product from cart successfully");
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/change")
    ResponseEntity<ResponseMessage<String>> change(@RequestBody Map<String, Object> body,
                                                   @SessionAttribute(value = "accEmail", required = false) String email,
                                                   HttpServletRequest request){
        long productId = decodeBase64ToLong(String.valueOf(body.get("pdId")));
        Integer qty = Integer.parseInt(String.valueOf(body.get("qty")));
        Account account = accountService.getAccount(email);
        Double delta = cartService.changeQuantity(account, productId, qty);
        request.getSession().setAttribute("sCart", account.getCart());
        ResponseMessage<String> responseMessage = new ResponseMessage<>(Response.SC_OK, "Change quantity successfully");
        // set data is total price of an item and total price of cart
        responseMessage.setData(Optional.of(delta.toString()));
        return ResponseEntity.ok(responseMessage);
    }

    private Long decodeBase64ToLong(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return Long.parseLong(new String(decodedBytes));
    }
}
