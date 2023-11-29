package com.example.ecommerce.response;

import com.example.ecommerce.model.User;

public class UserInfoResponse extends User {
    private Integer totalOrder;
    private Integer totalOrderSeller;

    public UserInfoResponse(User user, Integer totalOrder, Integer totalOrderSeller) {
        super(user.getId(),user.getName(), user.getEmail(), user.getPhoneNumber() ,user.getUsername(), user.getPassword(), user.getRole(), user.getAvatar(), user.getDob(), user.getProperty(), user.getMailPaypal());
        this.totalOrder = totalOrder;
        this.totalOrderSeller = totalOrderSeller;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }

    public Integer getTotalOrderSeller() {
        return totalOrderSeller;
    }

    public void setTotalOrderSeller(Integer totalOrderSeller) {
        this.totalOrderSeller = totalOrderSeller;
    }
}
