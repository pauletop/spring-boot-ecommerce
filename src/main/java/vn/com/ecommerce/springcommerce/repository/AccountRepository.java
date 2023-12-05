package vn.com.ecommerce.springcommerce.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecommerce.springcommerce.domain.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Iterable<Account> findByEmail(String email);

    Optional<Account> findAccountByEmail(String email);
    Page<Account> findAllByOrderById(Pageable page);
    void deleteById(Long id);
}