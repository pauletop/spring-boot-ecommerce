package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Order;
@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
