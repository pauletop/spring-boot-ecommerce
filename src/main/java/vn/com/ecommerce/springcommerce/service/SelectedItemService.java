package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.repository.SelectedItemRepository;

@Service
public class SelectedItemService {
    @Autowired
    SelectedItemRepository selectedItemRepository;
    @Transactional
    public void deleteByListAndProduct(Cart cart, Product product){
        selectedItemRepository.deleteBySelectedListAndProduct(cart, product);
    }

    public void deleteById(Long id) {
        selectedItemRepository.deleteById(id);
    }
}
