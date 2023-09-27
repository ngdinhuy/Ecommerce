package com.example.ecommerce.request;

import com.example.ecommerce.model.CategoryProduct;
import com.example.ecommerce.repository.CategoryProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryProductService {
    @Autowired
    CategoryProductRepository categoryProductRepository;

    public CategoryProduct addCategoryProduct(CategoryProduct categoryProduct){
        return categoryProductRepository.save(categoryProduct);
    }
}
