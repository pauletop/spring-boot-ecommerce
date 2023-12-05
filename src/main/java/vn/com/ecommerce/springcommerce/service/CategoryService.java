package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Category;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }
    public Page<Category> searchAdmin(String name, int page){
        return categoryRepository.findAllByNameContainingIgnoreCase(name,PageRequest.of(page, 15));
    }
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }
    public Category getById(Long id){
        return categoryRepository.findById(Math.toIntExact(id)).orElse(null);
    }
    public long count(){
        return categoryRepository.count();
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
    public Page<Category> getAllByPage(int page){
        Pageable pageable = PageRequest.of(page, 15);
        return categoryRepository.findAllByOrderById(pageable);
    }
    public void delete(Integer id){
        categoryRepository.deleteById(id);
    }

}
