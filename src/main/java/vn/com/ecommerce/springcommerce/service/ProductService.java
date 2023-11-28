package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Brand;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    final ProductRepository productRepository;
    final BrandService brandService;
    @Autowired
    public ProductService(ProductRepository productRepository, BrandService brandService) {
        this.productRepository = productRepository;
        this.brandService = brandService;
    }

    public void addProduct(Product product) {
        Brand brand = brandService.getBrandByName(product.getBrand().getName());
        // if brand not exist, create new brand
        // else set brand for product
        if (brand == null) {
            brand = product.getBrand();
            brandService.addBrand(brand);
        } else {
            product.setBrand(brand);
        }
        productRepository.save(product);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Page<Product> getAllProducts(int page) {
        return productRepository.findAll(PageRequest.of(page, 12));
    }

    public Iterable<Product> getTop5BestSellingProducts() {
        return productRepository.findTop5ByStockGreaterThanOrderBySoldDesc(0);
    }

    public Iterable<Product> getTop5NewestByCategory(Integer categoryId) {
        return productRepository.findTop5ByCategoryIdAndStockGreaterThanOrderByCreatedAtDesc(categoryId, 0);
    }

    public Iterable<Product> getTop5BestSellingProductsByCategory(Integer categoryId) {
        return productRepository.findTop5ByCategoryIdAndStockGreaterThanOrderBySoldDesc(categoryId, 0);
    }

    private Sort getSortOption(String sortOrder) {
        if (sortOrder != null) {
            if (sortOrder.equals("asc")) {
                return Sort.by("price").ascending();
            } else if (sortOrder.equals("desc")) {
                return Sort.by("price").descending();
            } else if (sortOrder.equals("hot")) {
                return Sort.by("sold").descending();
            } else if (sortOrder.equals("rating")) {
                return Sort.by("rating").descending();
            }
        }
        return Sort.unsorted();
    }

    public Page<Product> searchByKeyword(String keyword, Integer categoryId, int page, String sortOrder) {
        return productRepository.searchDefault(keyword, categoryId != 0 ? categoryId : null, PageRequest.of(page, 12, getSortOption(sortOrder)));
    }

    /**
     * Advance search, search by options
     * if no need to search by options, set null for that option,
     * but page must be set
     * @param keyword keyword to search
     * @param brandIds brandIds must be a list of brand id
     * @param categoryIds categoryIds must be a list of category id
     * @param minPrice min price filter
     * @param maxPrice maxPrice must be greater than minPrice
     * @param page page number
     * @param sortOrder asc, desc, hot or rating; you also can set null for no sort
     * @return An iterable of products
     */
    public Page<Product> searchByOptions(String keyword,
                                             List<Integer> brandIds,
                                             List<Integer> categoryIds,
                                             Double minPrice, Double maxPrice, int page, String sortOrder) {
        return productRepository.searchAdvance(keyword, brandIds, categoryIds, minPrice, maxPrice, PageRequest.of(page, 12, getSortOption(sortOrder)));
    }

    public Iterable<Product> getProductsByCategory(Integer categoryId, String sortOrder, int page) {
        return productRepository.findByCategoryId(categoryId, PageRequest.of(page, 12, getSortOption(sortOrder)));
    }
    public Iterable<Product> getProductsByBrand(Integer brandId, String sortOrder, int page) {
        return productRepository.findByBrandId(brandId, PageRequest.of(page, 12, getSortOption(sortOrder)));
    }

    public Iterable<Product> getProductsByBrandName(String brandName, String sortOrder, int page) {
        Brand brand = brandService.getBrandByName(brandName);
        if (brand == null) {
            return null;
        }
        return productRepository.findByBrandId(brand.getId(), PageRequest.of(page, 12, getSortOption(sortOrder)));
    }

    public Iterable<Product> getTop4RelatedProducts(Product product) {
        return productRepository.findTop4ByBrandIdAndPriceBetweenOrderBySoldDesc(product.getBrand().getId(), product.getPrice() - 180, product.getPrice() + 180);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


    /*---------------------------------*\
                UTILITY METHODS
    \*---------------------------------*/

}