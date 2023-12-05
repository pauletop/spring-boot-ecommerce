package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.CustomerReview;
import vn.com.ecommerce.springcommerce.domain.Product;

import java.util.List;

@Repository
public interface CustomerReviewRepository extends CrudRepository<CustomerReview, Long> {
    /**
     * Get all reviews of a product
     * and devided them by rating <br>
     * for example: 5 stars: 10 reviews,
     * 4 stars: 5 reviews,
     * 3 stars: 2 reviews,
     * 2 stars: 0 review,
     * 1 star: 0 review,
     * and store them in an integer list with 5 elements
     * @param productId
     * @return list contains 5 elements
     */
    @Query(value = "SELECT COUNT(*) FROM customer_review WHERE product_id = ?1 GROUP BY rating ORDER BY rating DESC",
            nativeQuery = true)
    List<Integer> getNumberOfReviewsByEachRating(Long productId);

    List<CustomerReview> getReviewsByProductId(Long productId);

    boolean existsByAccountAndProductId(Account account, Long productId);
}
