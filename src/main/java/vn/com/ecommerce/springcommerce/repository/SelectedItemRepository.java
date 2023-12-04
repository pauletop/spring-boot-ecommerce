package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.domain.SelectedItem;
import vn.com.ecommerce.springcommerce.domain.SelectedList;

@Repository
public interface SelectedItemRepository extends CrudRepository<SelectedItem, Long>{
    void deleteBySelectedListAndProduct(SelectedList list, Product product);
}