package com.example.ecommerce.response;

public class PagingBaseResponse {
    private Integer status;
    private String[] errors;
    private Object data;
    private Integer offset;

    public PagingBaseResponse(Integer status, String[] errors, Object data, Integer offset) {
        this.status = status;
        this.errors = errors;
        this.data = data;
        this.offset = offset;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
