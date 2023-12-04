package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.service.AccountService;
import vn.com.ecommerce.springcommerce.service.CartService;

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
    String index(@Nullable @CookieValue(value = "email", required = false) String email,
                 Model model) {
        return "cart";
    }

//    @PostMapping("/add")
//    String addItem(HttpServletRequest request) {
//        return "";
//    }
    private void setSessionAttribute(String email,
                                     Model model, HttpServletResponse response) {
        if (email != null) {
            Account account = accountService.getAccount(email);
            if (account != null) {
                Cookie emailCookie = new Cookie("email", email);
                emailCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
            }

        }
    }
}
