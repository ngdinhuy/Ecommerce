package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.relational.core.sql.In;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @Column(unique = true)
    private String email;
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private int role;

    private String avatar;

    private String dob;

    private String mailPaypal;

    private Boolean isOnline;

    private Double property;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Product> products;

    public User(Integer id, String name, String email, String phoneNumber, String username, String password, int role, String avatar, String dob, Double property, String mailPaypal) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.dob = dob;
        this.property = property;
        this.mailPaypal = mailPaypal;
    }




    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public User(String name, String username, String password, int role, Boolean isOnline, Double property) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isOnline = isOnline;
        this.property = property;
    }

    public User() {
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Double getProperty() {
        return property;
    }

    public void setProperty(Double property) {
        this.property = property;
    }

    public String getMailPaypal() {
        return mailPaypal;
    }

    public void setMailPaypal(String mailPaypal) {
        this.mailPaypal = mailPaypal;
    }
}
