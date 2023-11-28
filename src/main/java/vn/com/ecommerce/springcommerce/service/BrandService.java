package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Brand;
import vn.com.ecommerce.springcommerce.repository.BrandRepository;

@Service
public class BrandService {
    @Autowired
    BrandRepository brandRepository;

    public Brand getBrandByName(String name) {
        return brandRepository.findByName(name).orElse(null);
    }

    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Iterable<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
