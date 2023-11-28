package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.SelectedList;
@Repository
public interface SelectedListRepository extends CrudRepository<SelectedList, Long>{
}
