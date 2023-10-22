package com.example.ecommerce.request;

import com.example.ecommerce.model.CategoryProduct;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryProductService {
    @Autowired
    CategoryProductRepository categoryProductRepository;

    public CategoryProduct addCategoryProduct(CategoryProduct categoryProduct){
        return categoryProductRepository.save(categoryProduct);
    }

    public List<CategoryProduct> getCategoryProductByIdCategory(Integer idCategory){
        return categoryProductRepository.findCategoryProductsByCategoryId(idCategory);
    }

    public CategoryProduct getCategoryProductByProduct(Product product){
        return categoryProductRepository.findCategoryProductByProduct(product);
    }
}
