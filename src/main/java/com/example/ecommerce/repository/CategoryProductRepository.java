package com.example.ecommerce.repository;

import com.example.ecommerce.model.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Integer> {
    public List<CategoryProduct> findCategoryProductsByCategoryId(int category_id);
}
