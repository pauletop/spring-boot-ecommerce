package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.ecommerce.springcommerce.domain.*;
import vn.com.ecommerce.springcommerce.service.*;

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
                                  @RequestParam(name="page", required = false) Integer page,
                                  Model model, HttpServletRequest request){
//        model.addAttribute("isLogin", (boolean) true);

        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        if (page==null){
            page=1;
        }
        List<Product> productsPage = productService.getAllProducts(0).getContent();
        for(Product product:productsPage){
            System.out.println(product);
        }
        model.addAttribute("products",productsPage);
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
                                @RequestParam(name = "product-description") String productDescription
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
        }
        catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return "redirect:/admin/products";
    }
}