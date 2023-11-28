package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Brand;

import java.util.Optional;

@Repository
public interface BrandRepository extends CrudRepository<Brand, Integer> {
    Optional<Brand> findByName(String name);
}
