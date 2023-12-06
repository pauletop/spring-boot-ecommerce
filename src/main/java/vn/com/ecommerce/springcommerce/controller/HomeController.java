package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.service.AccountService;
import vn.com.ecommerce.springcommerce.service.CategoryService;
import vn.com.ecommerce.springcommerce.service.ProductService;

import java.util.List;

@Controller
public class HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    @Autowired
    public HomeController(ProductService productService, CategoryService categoryService, AccountService accountService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.accountService = accountService;
    }

    @GetMapping({"", "/", "/home"})
    String index(Model model, @Nullable @SessionAttribute(value = "sCart", required = false) Cart cart,
                    @Nullable @SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                 @Nullable @SessionAttribute(value = "accEmail", required = false) String email){
        List<Product> topLaps = (List<Product>) productService.getTop5BestSellingProductsByCategory(1);
        List<Product> newLaps = (List<Product>) productService.getTop5NewestByCategory( 1);
        List<Product> topPhones = (List<Product>) productService.getTop5BestSellingProductsByCategory(2);
        List<Product> newPhones = (List<Product>) productService.getTop5NewestByCategory(2);
        List<Product> topAcces = (List<Product>) productService.getTop5BestSellingProductsByCategory(3);
        List<Product> newAcces = (List<Product>) productService.getTop5NewestByCategory(3);
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
        }
        if (email!=null){
            Account account = accountService.getAccount(email);
            model.addAttribute("user", account);
        }
        model.addAttribute("topLaps", topLaps);
        model.addAttribute("newLaps", newLaps);
        model.addAttribute("topPhones", topPhones);
        model.addAttribute("newPhones", newPhones);
        model.addAttribute("topAcces", topAcces);
        model.addAttribute("newAcces", newAcces);
        model.addAttribute("sCart", cart);
        model.addAttribute("navActive", "home");
        return "index";
    }
}