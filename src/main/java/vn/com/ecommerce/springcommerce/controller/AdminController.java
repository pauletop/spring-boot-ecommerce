package vn.com.ecommerce.springcommerce.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.ecommerce.springcommerce.domain.*;
import vn.com.ecommerce.springcommerce.service.*;

import java.io.IOException;
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
        }
        if (page == null) {
            page = 1;
        }

        Page<Product> productsPage = productService.getAllProducts(page - 1);
        model.addAttribute("noname","true");
        return getString(page, model, session, productsPage);
    }
    @GetMapping("/products/search")
    public String getProductAdminSearch(@SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                                  @SessionAttribute(value = "accEmail", required = false) String accEmail,
                                  @RequestParam(name="page", required = false) Integer page,
                                  @RequestParam(name="name",required=false) String name,
                                  Model model, HttpSession session, HttpServletResponse response) throws IOException {
//        model.addAttribute("isLogin", (boolean) true);
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        if (page == null) {
            page = 1;
        }

        Page<Product> productsPage = productService.searchAdmin(name, page - 1);
        model.addAttribute("name",name);
        return getString(page, model, session, productsPage);
    }

    private String getString(@RequestParam(name = "page", required = false) Integer page, Model model, HttpSession session, Page<Product> productsPage) {
        List<Product> products = productsPage.getContent();
        model.addAttribute("products", products);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("success", session.getAttribute("success"));
        model.addAttribute("error", session.getAttribute("error"));
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("totalProducts", productsPage.getTotalElements());
        session.setAttribute("adminPage", "Products");
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
    ) {
        Brand brand = brandService.getBrandByName(productBrand);
        Category category = categoryService.getCategoryByName(productCategory);
        Product product = new Product();
        if (productId <= productService.count()) {
            product = productService.getProductById(productId);
        }
        product.setName(productName);
        product.setPrice(productPrice);
        product.setBrand(brand);
        product.setCategory(category);
        product.setColor(productColor);
        product.setStock(productStock);
        product.setSold(productSold);
        product.setDescription(productDescription);
        try {
            productService.saveProduct(product);
            session.setAttribute("success", "Succeed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed.");
        }

        return "redirect:/admin/products";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam(name = "id") Long id, HttpSession session) {
        try {
            productService.deleteById(id);
            session.setAttribute("success", "Delete successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed to delete.");

        }
        return "redirect:/admin/products";
    }

    @GetMapping("/users")
    public String getUserAdmin(@SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                               @RequestParam(name = "page", required = false) Integer page,
                               @RequestParam(name="name",required =false) String name,
                               Model model, HttpSession session) {
//        model.addAttribute("isLogin", (boolean) true);

        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        if (page == null) {
            page = 1;
        }
        Page<Account> accountPage = null;
        if(name!=null){
            accountPage=accountService.searchAdmin(name,page-1);
            model.addAttribute("email",name);
        }else{
            accountPage = accountService.getAllOrderById(page - 1);
        }
        model.addAttribute("accountPage", accountPage);
        model.addAttribute("success", session.getAttribute("success"));
        model.addAttribute("error", session.getAttribute("error"));
        model.addAttribute("totalPages", accountPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalProducts", accountPage.getTotalElements());
        session.setAttribute("adminPage", "Users");
        session.removeAttribute("success");
        session.removeAttribute("error");
        return "admin/adminUsers";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam(name = "id") Long id, HttpSession session) {
        try {
            accountService.delete(id);
            session.setAttribute("success", "Delete successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed to delete.");

        }
        return "redirect:/admin/users";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(name = "item-id") Long itemId,
                             @RequestParam(name = "item-fullname") String itemFullName,
                             @RequestParam(name = "item-password") String itemPassword,
                             @RequestParam(name = "item-email") String itemEmail,
                             @RequestParam(name = "item-phone") String itemPhone,
                             @RequestParam(name = "item-role") String itemRole,
                             @RequestParam(name = "item-status") boolean itemStatus,
                             HttpSession session
    ) {
        Account user = new Account();
        if (itemId< accountService.count()) {
            // Handle the case where the user with the given ID is not found
           user = accountService.getAccount(itemId);
        }

        user.setFullname(itemFullName);
        user.setPassword(itemPassword);
        user.setEmail(itemEmail);
        user.setPhone(itemPhone);
        if(itemRole.equals("ROLE_ADMIN")){
            user.setRole(Role.ROLE_ADMIN);
        }
        user.setStatus(itemStatus);

        try {
            accountService.register(user);
            session.setAttribute("success", "Succeed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed.");
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/brands")
    public String getBrandAdmin(@SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                                @RequestParam(name = "page", required = false) Integer page,
                                @RequestParam(name = "name", required = false) String name,
                                Model model, HttpSession session) {
//        model.addAttribute("isLogin", (boolean) true);

        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        Page<Brand> brandPage = null;
        if (page == null) {
            page = 1;
        }
        if (name!=null){
            brandPage=brandService.searchAdmin(name,page-1);
            model.addAttribute("name",name);

        }else{
            brandPage = brandService.getAllByPage(page - 1);
        }
        model.addAttribute("brandPage", brandPage);
        model.addAttribute("success", session.getAttribute("success"));
        model.addAttribute("error", session.getAttribute("error"));
        model.addAttribute("currentPage", page);
        session.setAttribute("adminPage", "Brands");
        session.removeAttribute("success");
        session.removeAttribute("error");
        return "admin/adminBrands";
    }
    @PostMapping("/deleteBrand")
    public String deleteBrand(@RequestParam(name = "id") Integer id, HttpSession session) {
        try {
            brandService.delete(id);
            session.setAttribute("success", "Delete successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed to delete.");

        }
        return "redirect:/admin/brands";
    }
    @PostMapping("/updateBrand")
    public String updateUser(@RequestParam(name = "brand-id") Integer id,
                             @RequestParam(name = "brand-name") String name,
                             HttpSession session
    ) {
        Brand brand = new Brand();
        if (id< brandService.count()) {
            // Handle the case where the user with the given ID is not found
           brand = brandService.getById(id);
        }

        brand.setName(name);

        try {
            brandService.addBrand(brand);
            session.setAttribute("success", "Succeed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed.");
        }

        return "redirect:/admin/brands";
    }
    @GetMapping("/categories")
    public String getCategoryAdmin(@SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                                   @RequestParam(name = "page", required = false) Integer page,
                                   @RequestParam(name = "name", required = false) String name,
                                   Model model, HttpSession session) {
//        model.addAttribute("isLogin", (boolean) true);

        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        if (page == null) {
            page = 1;
        }
        Page<Category> brandPage = null;
        if (name!=null){
            brandPage = categoryService.searchAdmin(name,page-1);
            model.addAttribute("name",name);

        }
        else{
            brandPage = categoryService.getAllByPage(page-1);
        }
        model.addAttribute("brandPage", brandPage);
        model.addAttribute("success", session.getAttribute("success"));
        model.addAttribute("error", session.getAttribute("error"));
        model.addAttribute("currentPage", page);
        session.setAttribute("adminPage", "Categories");
        session.removeAttribute("success");
        session.removeAttribute("error");
        return "admin/adminCategories";
    }
    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam(name = "id") Integer id, HttpSession session) {
        try {
            categoryService.delete(id);
            session.setAttribute("success", "Delete successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed to delete.");

        }
        return "redirect:/admin/categories";
    }
    @PostMapping("/updateCategory")
    public String updateCategory(@RequestParam(name = "brand-id") Long id,
                             @RequestParam(name = "brand-name") String name,
                             HttpSession session
    ) {
        Category category = new Category();
        if (id< categoryService.count()) {
            // Handle the case where the user with the given ID is not found
            category = categoryService.getById(id);
        }

        category.setName(name);

        try {
            categoryService.addCategory(category);
            session.setAttribute("success", "Succeed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.setAttribute("error", "Failed.");
        }

        return "redirect:/admin/categories";
    }

}