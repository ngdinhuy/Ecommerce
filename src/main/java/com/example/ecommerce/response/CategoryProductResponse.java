package com.example.ecommerce.response;

import com.example.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryProductResponse {
    Integer idCategory;
    String titleCategory, descriptionCategory;

    List<Product> products = new ArrayList<>();

    public CategoryProductResponse() {
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public String getTitleCategory() {
        return titleCategory;
    }

    public void setTitleCategory(String titleCategory) {
        this.titleCategory = titleCategory;
    }

    public String getDescriptionCategory() {
        return descriptionCategory;
    }

    public void setDescriptionCategory(String descriptionCategory) {
        this.descriptionCategory = descriptionCategory;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
