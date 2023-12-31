package com.example.ecommerce.model;

import com.example.ecommerce.utils.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

@Entity
@Table(name = "tbl_product")
public class Product implements Serializable {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(length = 3000)
    private String description;

    private int quantity;

    private double price;

    private double discount;

    private String reviewNumber;

    private double rate;
    @Lob
    @Column(length = 10000)
    @Convert(converter = StringListConverter.class)
    private List<String> image;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<CartItem> listCartItem;

    @Transient
    private String categoryName;

    @Transient
    private Integer idCategory;

    //    public Product(String name, String descriptiopn, int quantity, int price, int discount, String reviewNumber, double rate, String image, Seller sellerid) {
//        this.name = name;
//        this.descriptiopn = descriptiopn;
//        this.quantity = quantity;
//        this.price = price;
//        this.discount = discount;
//        this.reviewNumber = reviewNumber;
//        this.rate = rate;
//        this.image = image;
//        this.sellerid = sellerid;
//    }

    public Product() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descriptiopn) {
        this.description = descriptiopn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getReviewNumber() {
        return reviewNumber;
    }

    public void setReviewNumber(String reviewNumber) {
        this.reviewNumber = reviewNumber;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public User getSellerid() {
        return user;
    }

    public void setSellerid(User sellerid) {
        this.user = sellerid;
    }

    public Double getPriceProduct() {
        if (discount != 0) {
            return Double.parseDouble(new DecimalFormat("##.##").format(price * (100 - discount) / 100));
        } else {
            return price;
        }
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }
}
