package com.example.ecommerce.request;

public class UpdateProductRequest {
    private String description;

    private Integer discount;

    private String image, name;

    private Integer price,quantity;
    private Double rate;
    private String reviewNumber;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String avatar) {
        this.image = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getReviewNumber() {
        return reviewNumber;
    }

    public void setReviewNumber(String reviewNumber) {
        this.reviewNumber = reviewNumber;
    }
}
