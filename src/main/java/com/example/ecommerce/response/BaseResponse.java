package com.example.ecommerce.response;

public class BaseResponse {
     private String status;
     private String[] errors;
     private Object data;

    public BaseResponse(String status, String[] errors, Object data) {
        this.status = status;
        this.errors = errors;
        this.data = data;
    }
}
