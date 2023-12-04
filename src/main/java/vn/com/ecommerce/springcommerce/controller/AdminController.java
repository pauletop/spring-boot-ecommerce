package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.service.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    final CartService cartService;
    final AccountService accountService;

    public AdminController(ProductService productService, CategoryService categoryService, BrandService brandService, CartService cartService, AccountService accountService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.cartService = cartService;
        this.accountService = accountService;
    }

    @GetMapping("/admin/products")
    public String getProductAdmin(@Nullable @SessionAttribute(value = "accEmail", required = false) String email,
                                  @SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                                  @Nullable @SessionAttribute(value = "sCart", required = false) Cart sCart,
                                  Model model, HttpServletRequest request){
        if (email == null) {
            return "redirect:/account/login";
        }
//        model.addAttribute("isLogin", (boolean) true);

        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        Page<Product> productsPage = productService.getAllProducts(1);
        System.out.println(Arrays.toString(productsPage.stream().toArray()));
        List<Product> products = new ArrayList<>(productsPage.getContent());
        model.addAttribute("products",products);
        return "admin/adminProducts";
    }
}
