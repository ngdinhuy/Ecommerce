package com.example.ecommerce.response;

import java.util.List;

public class OrderItemResponse {
    Integer idOrder;
    Integer statePayment;
    Integer idProduct;
    String nameProduct;
    Double priceProduct;
    Integer idUSer;
    Integer quantity;
    Double totalPrice;

    String nameUser;

    List<String> images;

    public OrderItemResponse(Integer idOrder, Integer statePayment, Integer idProduct, String nameProduct, Double priceProduct, Integer idUSer, Integer quantity, String nameUser, Double totalPrice, List<String> images) {
        this.idOrder = idOrder;
        this.statePayment = statePayment;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.idUSer = idUSer;
        this.quantity = quantity;
        this.nameUser = nameUser;
        this.totalPrice = totalPrice;
        this.images = images;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public Integer getStatePayment() {
        return statePayment;
    }

    public void setStatePayment(Integer statePayment) {
        this.statePayment = statePayment;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(Double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public Integer getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(Integer idUSer) {
        this.idUSer = idUSer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
