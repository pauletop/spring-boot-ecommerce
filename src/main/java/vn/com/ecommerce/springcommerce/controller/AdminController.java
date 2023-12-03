package vn.com.ecommerce.springcommerce.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
public class AdminController {
    @GetMapping("/admin/products")
    public String getProductAdmin(Model model){
        return "/admin/productAdmin";
    }
}
