package com.example.ecommerce.response;

import java.io.Serializable;

public class BaseResponse implements Serializable {
     private Integer status;
     private String[] errors;
     private Object data;

    public BaseResponse(int status, String[] errors, Object data) {
        this.status = status;
        this.errors = errors;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getErrors() {
        return errors;
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
