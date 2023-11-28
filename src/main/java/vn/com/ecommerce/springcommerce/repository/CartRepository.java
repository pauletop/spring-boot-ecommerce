package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Cart;
@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
}
