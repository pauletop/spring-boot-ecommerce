package vn.com.ecommerce.springcommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.ecommerce.springcommerce.DTO.ResponseMessage;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.CustomerReview;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.service.CustomerReviewService;
import vn.com.ecommerce.springcommerce.service.ProductService;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CustomerReviewService customerReviewService;
    @Autowired
    public ProductController(ProductService productService, CustomerReviewService customerReviewService) {
        this.productService = productService;
        this.customerReviewService = customerReviewService;
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
        Product product = productService.getProductById(id);
        // lấy số lượng đánh giá theo từng sao
        List<CustomerReview> reviews = product.getCustomerReviews();
        int[] numberOfReviewsByEachRating = new int[5];
        for (CustomerReview review : reviews) {
            numberOfReviewsByEachRating[review.getRating() - 1]++;
        }
        Cart sCart = (Cart) request.getSession().getAttribute("sCart");
        Boolean isLogin = (Boolean) request.getSession().getAttribute("isLogin");
        model.addAttribute("sCart", sCart);
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
        }
        List<Product> relatedProducts = (List<Product>) productService.getTop4RelatedProducts(product);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("numberOfReviewsByEachRating", numberOfReviewsByEachRating);
        model.addAttribute("product", product);
        model.addAttribute("breadcrumbs", new String[]{category, brand});
        model.addAttribute("navActive", "product");
        return "product";
    }

    @PostMapping("/review")
    ResponseEntity<ResponseMessage<String>> reviewProduct(@RequestBody Map<String, String> body, @SessionAttribute(value = "accEmail", required = false) String email) {
        if (email == null) {
            ResponseMessage<String> responseMessage = new ResponseMessage<>(Response.SC_UNAUTHORIZED, "You must login to review this product");
            return ResponseEntity.status(Response.SC_UNAUTHORIZED).body(responseMessage);
        }
        String productId = body.get("pId");
        String rating = body.get("rating");
        String comment = body.get("comment");
        String displayName = body.get("dName");
        CustomerReview customerReview;
        try {
            Long id = Long.parseLong(new String(Base64.getDecoder().decode(productId)));
            byte rate = Byte.parseByte(rating);
            if (customerReviewService.isReviewed(email, id)) {
                return ResponseEntity.status(Response.SC_BAD_REQUEST).body(new ResponseMessage<>(Response.SC_BAD_REQUEST, "You have already reviewed this product"));
            }
            if (rate < 1 || rate > 5) {
                return ResponseEntity.status(Response.SC_BAD_REQUEST).body(new ResponseMessage<>(Response.SC_BAD_REQUEST, "Rating must be between 1 and 5"));
            }
            customerReview = customerReviewService.addReview(displayName, email, id, comment, rate);
            if (customerReview == null) {
                return ResponseEntity.status(Response.SC_BAD_REQUEST).body(new ResponseMessage<>(Response.SC_BAD_REQUEST, "Information is not valid"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(Response.SC_BAD_REQUEST).body(new ResponseMessage<>(Response.SC_BAD_REQUEST, "Something went wrong, please try again!"));
        }
        ResponseMessage<String> responseMessage = new ResponseMessage<>(Response.SC_OK, "Review added successfully");
        return ResponseEntity.ok(responseMessage);
    }
}
