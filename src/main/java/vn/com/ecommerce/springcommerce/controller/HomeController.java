package vn.com.ecommerce.springcommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.com.ecommerce.springcommerce.domain.Category;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.service.CategoryService;
import vn.com.ecommerce.springcommerce.service.ProductService;

import java.util.List;

@Controller
public class HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;
    @Autowired
    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({"", "/", "/home"})
    String index(Model model) {
        List<Product> topLaps = (List<Product>) productService.getTop5BestSellingProductsByCategory(1);
        List<Product> newLaps = (List<Product>) productService.getTop5NewestByCategory( 1);
        List<Product> topPhones = (List<Product>) productService.getTop5BestSellingProductsByCategory(2);
        List<Product> newPhones = (List<Product>) productService.getTop5NewestByCategory(2);
        List<Product> topAcces = (List<Product>) productService.getTop5BestSellingProductsByCategory(3);
        List<Product> newAcces = (List<Product>) productService.getTop5NewestByCategory(3);
        model.addAttribute("topLaps", topLaps);
        model.addAttribute("newLaps", newLaps);
        model.addAttribute("topPhones", topPhones);
        model.addAttribute("newPhones", newPhones);
        model.addAttribute("topAcces", topAcces);
        model.addAttribute("newAcces", newAcces);
        return "index";
    }


}
