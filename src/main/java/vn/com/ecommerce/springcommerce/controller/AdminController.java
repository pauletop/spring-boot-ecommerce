package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.ecommerce.springcommerce.domain.*;
import vn.com.ecommerce.springcommerce.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
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

    @GetMapping("/products")
    public String getProductAdmin(@SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                                  @SessionAttribute(value = "accEmail", required = false) String accEmail,
                                  @RequestParam(name="page", required = false) Integer page,
                                  Model model, HttpSession session, HttpServletResponse response) throws IOException {
//        model.addAttribute("isLogin", (boolean) true);
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
            Account account = accountService.getAccount(accEmail);
            if (account.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))) {
                System.out.println(true);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        if (page==null){
            page=1;
        }
        Page<Product> productsPage = productService.getAllProducts(page-1);
        List<Product> products = productsPage.getContent();
        model.addAttribute("products",products);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("success",session.getAttribute("success"));
        model.addAttribute("error",session.getAttribute("error"));
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalProducts", productsPage.getTotalElements());
        session.removeAttribute("success");
        session.removeAttribute("error");
        return "admin/adminProducts";
    }
    @PostMapping("/updateProduct")
    public String updateProduct(@RequestParam(name = "product-id") Long productId,
                                @RequestParam(name = "product-name") String productName,
                                @RequestParam(name = "product-price") double productPrice,
                                @RequestParam(name = "product-category") String productCategory,
                                @RequestParam(name = "product-brand") String productBrand,
                                @RequestParam(name = "product-color") String productColor,
                                @RequestParam(name = "product-stock") Integer productStock,
                                @RequestParam(name = "product-sold") Integer productSold,
                                @RequestParam(name = "product-description") String productDescription,
                                HttpSession session
    ){
        Brand brand = brandService.getBrandByName(productBrand);
        Category category = categoryService.getCategoryByName(productCategory);
        Product product = productService.getProductById(productId);
        product.setName(productName);
        product.setPrice(productPrice);
        product.setBrand(brand);
        product.setCategory(category);
        product.setColor(productColor);
        product.setStock(productStock);
        product.setSold(productSold);
        product.setDescription(productDescription);
        try{
            productService.saveProduct(product);
            session.setAttribute("success","Succeed.");
        }
        catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
            session.setAttribute("error","Failed.");

        }

        return "redirect:/admin/products";
    }
    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam(name="id") Long id, HttpSession session){
        try{
            productService.deleteById(id);
            session.setAttribute("success","Delete successfully");
        }
        catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
            session.setAttribute("error","Failed to delete.");

        }
        return "redirect:/admin/products";
    }
}