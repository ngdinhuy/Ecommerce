package com.example.ecommerce.utils;

import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {

    public static ResponseEntity<BaseResponse> getResponse(Integer statusCodeResponse, String[] errors, Object data){
        return ResponseEntity.status(HttpStatus.OK).body(
            new BaseResponse(statusCodeResponse, errors, data)
        );
    }
    public static void updateUser(User user, User updateUser){
        user.setEmail(updateUser.getEmail());
        user.setName(updateUser.getName());
        user.setAvatar(updateUser.getAvatar());
        user.setPassword(updateUser.getPassword());
        user.setPhoneNumber(updateUser.getPhoneNumber());
    }
}
