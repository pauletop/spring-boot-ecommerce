package vn.com.ecommerce.springcommerce.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Account;
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Iterable<Account> findByEmail(String email);


}