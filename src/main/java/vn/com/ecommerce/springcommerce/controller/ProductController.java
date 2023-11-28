package vn.com.ecommerce.springcommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.com.ecommerce.springcommerce.domain.CustomerReview;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.service.ProductService;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    public String getBrandName(String brandName){
        return switch (brandName) {
            case "apple" -> "Apple";
            case "asus" -> "Asus";
            case "dell" -> "Dell";
            case "hp" -> "HP";
            case "lenovo" -> "Lenovo";
            case "lg" -> "Lg";
            case "samsung" -> "Samsung";
            case "oppo" -> "OPPO";
            case "xiaomi" -> "Xiaomi";
            case "baseus" -> "Baseus";
            default -> null;
        };
    }
    //get link with pattern: /laptop/{brand}/*-{productId}
    @GetMapping("{category}/{brand}/*-{productId}")
    public String showProduct(Model model,
                              @PathVariable String category,
                              @PathVariable String brand,
                              @PathVariable String productId,
                              HttpServletRequest request) {
        // Xử lý logic của bạn với brand và productId
        long id = Long.parseLong(new String(Base64.getDecoder().decode(productId)));
        System.out.println("Category: " + category);
        System.out.println("Brand: " + brand);
        System.out.println("Product ID: " + id);
        System.out.println("Brand name: " + getBrandName(brand));
        Product product = productService.getProductById(id);
        // lấy số lượng đánh giá theo từng sao
        List<CustomerReview> reviews = product.getCustomerReviews();
        int[] numberOfReviewsByEachRating = new int[5];
        for (CustomerReview review : reviews) {
            numberOfReviewsByEachRating[review.getRating() - 1]++;
        }
        List<Product> relatedProducts = (List<Product>) productService.getTop4RelatedProducts(product);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("numberOfReviewsByEachRating", numberOfReviewsByEachRating);
        model.addAttribute("product", product);
        model.addAttribute("breadcrumbs", new String[]{category, brand});
        return "product";
    }
}
