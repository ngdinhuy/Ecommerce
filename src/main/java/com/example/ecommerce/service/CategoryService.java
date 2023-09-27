package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<Category> getListCategory(){
        return repository.findAll();
    }

    public List<Category> getListCategoryFollowTitle(String title){
        return repository.getCategoriesByTitle(title);
    }

    public Category addCategory(Category category){
        return repository.save(category);
    }

    public Category getCategoryById(Integer id){ return repository.getCategoryById(id);}
}
