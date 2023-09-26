package com.example.ecommerce.Resources;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/category")
public class CategoryResource {
    @Autowired
    CategoryService categoryService;

    @GetMapping()
    ResponseEntity<BaseResponse> getAllCategory(){
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, categoryService.getListCategory());
    }

    @PostMapping()
    ResponseEntity<BaseResponse> addCategory(@RequestBody Category category){
        List<Category> categoriesExist = categoryService.getListCategoryFollowTitle(category.getTitle());
        if (!categoriesExist.isEmpty()){
            return Utils.getResponse(HttpStatus.CONTINUE.value(), new String[]{"Category is exist"},"");
        } else {
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, categoryService.addCategory(category));
        }
    }
}
