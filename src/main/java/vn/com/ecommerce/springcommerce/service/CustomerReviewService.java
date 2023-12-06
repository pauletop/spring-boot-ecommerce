package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.CustomerReview;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.repository.CustomerReviewRepository;

import java.util.List;

@Service
public class CustomerReviewService {
    @Autowired
    CustomerReviewRepository customerReviewRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ProductService productService;

    public CustomerReview addReview(String displayName, String accountEmail, Long productId, String review, byte rating) {
        CustomerReview customerReview = new CustomerReview();
        Account account = accountService.getAccount(accountEmail);
        if (account == null) {
            return null;
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            return null;
        }
        customerReview.setData(account, displayName, product, rating, review);
        product.addRating(rating);
        productService.saveProduct(product);
        return customerReviewRepository.save(customerReview);
    }

    public boolean isReviewed(String accountEmail, Long productId) {
        Account account = accountService.getAccount(accountEmail);
        if (account == null) {
            return false;
        }
        return customerReviewRepository.existsByAccountAndProductId(account, productId);
    }

    /**
     * Get all reviews of a product
     * and devide them by rating <br>
     * for example: 5 stars: 10 reviews,
     * 4 stars: 5 reviews,
     * 3 stars: 2 reviews,
     * 2 stars: 0 review,
     * 1 star: 0 review,
     * and store them in an integer list with 5 elements
     * @param productId id of product
     * @return list contains 5 elements
     */
    public List<Integer> getNumberOfReviewsByEachRating(Long productId) {
        List<Integer> numberOfReviewsByEachRating = customerReviewRepository.getNumberOfReviewsByEachRating(productId);
        return numberOfReviewsByEachRating;
    }

    public List<CustomerReview> getReviewsByProductId(Long productId) {
        return customerReviewRepository.getReviewsByProductId(productId);
    }
}
