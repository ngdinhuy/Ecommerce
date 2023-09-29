package com.example.ecommerce.Resources;

import com.example.ecommerce.model.*;
import com.example.ecommerce.request.AddProductRequest;
import com.example.ecommerce.request.CategoryProductService;
import com.example.ecommerce.request.UpdateProductRequest;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.response.CategoryProductResponse;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProdcutService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        addedProduct.setDescription(request.getDescription());
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

    @GetMapping(path = "/category/{id}")
    ResponseEntity<BaseResponse> getProductByCategoryId(@PathVariable int id){
        List<CategoryProduct> categoryProducts = categoryProductService.getCategoryProductByIdCategory(id);
        if (categoryProducts.isEmpty()){
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{"The category is empty"}, null);
        }
        ArrayList<Product> products = new ArrayList<>();
        for(CategoryProduct categoryProduct: categoryProducts){
            products.add(prodcutService.getProductById(categoryProduct.getProduct().getId()));
        }
        if(products.isEmpty()){
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{"The category is empty"}, null);
        } else {
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, products);
        }
    }

    @GetMapping(path = "/all")
    ResponseEntity<BaseResponse> getAllProduct(){
        List<Product> products = prodcutService.getAllProduct();
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, products);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity<BaseResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable int id){
        Product updatedProduct = prodcutService.getProductById(id);
        if (updatedProduct == null){
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Product not exist"}, null);
        }
        if (request.getDescription()!=null){
            updatedProduct.setDescription(request.getDescription());
        }
        if (request.getDiscount() != null){
            updatedProduct.setDiscount(request.getDiscount());
        }
        if (request.getImage() != null){
            updatedProduct.setImage(request.getImage());
        }
        if (request.getName() != null){
            updatedProduct.setName(request.getName());
        }
        if (request.getPrice() != null){
            updatedProduct.setPrice(request.getPrice());
        }
        if (request.getQuantity() != null){
            updatedProduct.setQuantity(request.getQuantity());
        }
        if (request.getRate() != null){
            updatedProduct.setRate(request.getRate());
        }
        if (request.getReviewNumber() != null) {
            updatedProduct.setReviewNumber(request.getReviewNumber());
        }
        try{
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, prodcutService.updateProduct(updatedProduct));
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @GetMapping(path = "/category_product")
    ResponseEntity<BaseResponse> getCategoryAndProduct(){
        List<Category> categories = categoryService.getListCategory();
        ArrayList<CategoryProductResponse> response = new ArrayList<>();
        for(Category category: categories){
            CategoryProductResponse categoryProductResponse = new CategoryProductResponse();
            categoryProductResponse.setIdCategory(category.getId());
            categoryProductResponse.setTitleCategory(category.getTitle());
            categoryProductResponse.setDescriptionCategory(category.getDescription());

            List<CategoryProduct> categoryProducts = categoryProductService.getCategoryProductByIdCategory(category.getId());
            if (categoryProducts.isEmpty()){
                continue;
            }
            ArrayList<Product> products = new ArrayList<>();
            for(CategoryProduct categoryProduct: categoryProducts){
                products.add(prodcutService.getProductById(categoryProduct.getProduct().getId()));
            }
            if(products.size()<=5){
                categoryProductResponse.setProducts(products);
                response.add(categoryProductResponse);
            }
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, response);
    }
}