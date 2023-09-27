package com.example.ecommerce.Resources;

import com.example.ecommerce.model.*;
import com.example.ecommerce.request.AddProductRequest;
import com.example.ecommerce.request.CategoryProductService;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProdcutService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Utils;
import org.slf4j.helpers.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductResouce {
    @Autowired
    ProdcutService prodcutService;


    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryProductService categoryProductService;

    @PostMapping(path = "/insert")
    ResponseEntity<BaseResponse> insertProduct(@RequestBody AddProductRequest request){
        //kiem tra user co ton tai khong
        User seller = userService.findUserById(request.getIdSeller());
        if (seller == null){
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Cant find seller"}, null);
        }
        Product addedProduct = new Product();
        addedProduct.setSellerid(seller);
        addedProduct.setName(request.getName());
        addedProduct.setDescriptiopn(request.getDescription());
        addedProduct.setQuantity(request.getQuantity());
        addedProduct.setPrice(request.getPrice());
        addedProduct.setDiscount(request.getDiscount());
        addedProduct.setReviewNumber("0");
        addedProduct.setRate(0.0);
        addedProduct.setImage(request.getImage());
        try {
            Product product = prodcutService.insertProduct(addedProduct);
            Category category = categoryService.getCategoryById(request.getIdCategory());
            CategoryProduct categoryProduct = new CategoryProduct(category, product);
            categoryProductService.addCategoryProduct(categoryProduct);
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, product);
        } catch (Exception e){
            return Utils.getResponse(HttpStatus.NO_CONTENT.value(), new String[] {e.getMessage()}, null);
        }
    }

    @GetMapping(path = "/seller/{id}")
    ResponseEntity<BaseResponse> getProductsBySellerId(@PathVariable int id){
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, prodcutService.getProductBySellerId(id));
    }
}
