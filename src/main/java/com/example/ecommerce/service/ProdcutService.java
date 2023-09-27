package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.request.AddProductRequest;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ProdcutService {
    @Autowired
    private ProductRepository repository;

    public Product insertProduct(Product product){
        return repository.save(product);
    }

    public List<Product> getProductBySellerId(int id){
        return repository.getProductsByUserId(id);
    }

}
