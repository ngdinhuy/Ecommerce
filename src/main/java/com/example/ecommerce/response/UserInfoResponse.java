package com.example.ecommerce.response;

import com.example.ecommerce.model.User;

public class UserInfoResponse extends User {
    private Integer totalOrder;

    public UserInfoResponse(User user, Integer totalOrder) {
        super(user.getId(),user.getName(), user.getEmail(), user.getPhoneNumber() ,user.getUsername(), user.getPassword(), user.getRole(), user.getAvatar(), user.getDob(), user.getProperty(), user.getMailPaypal());
        this.totalOrder = totalOrder;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }
}
