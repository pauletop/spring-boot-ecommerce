package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    Iterable<Product> findTop10ByOrderByCreatedAtDesc();

    Iterable<Product> findTop10ByStockGreaterThanOrderBySoldDesc(int i);

    Iterable<Product> findTop5ByCategoryIdAndStockGreaterThanOrderByCreatedAtDesc(Integer categoryId, int i);

    Iterable<Product> findTop5ByCategoryIdAndStockGreaterThanOrderBySoldDesc(Integer categoryId, int i);

    Iterable<Product> findTop5ByStockGreaterThanOrderBySoldDesc(int i);

    @Query("SELECT p FROM Product p " +
            "WHERE (:keyword IS NULL OR " +
            "LOWER(p.name) LIKE %:keyword% OR " +
            "LOWER(p.brand.name) LIKE %:keyword% OR " +
            "LOWER(p.color) LIKE %:keyword%) " +
            "AND (:brandIds IS NULL OR p.brand.id IN :brandIds) " +
            "AND (:categoryIds IS NULL OR p.category.id IN :categoryIds) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> searchAdvance(
            @Param("keyword") String keyword,
            @Param("brandIds") List<Integer> brandIds,
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE (:keyword IS NULL OR " +
            "LOWER(p.name) LIKE %:keyword% OR " +
            "LOWER(p.brand.name) LIKE %:keyword% OR " +
            "LOWER(p.color) LIKE %:keyword%) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> searchDefault(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    Page<Product> findByBrandId(Integer brandId, Pageable pageable);

    Iterable<Product> findTop4ByBrandIdAndPriceBetweenOrderBySoldDesc(Integer id, double min, double max);
    Page<Product> findAllByOrderById(Pageable page);

    void deleteById(Long id);
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

}