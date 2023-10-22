package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;

@Entity
@Table(name = "tbl_statistic_monthly")
public class StatisticMonthly {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    private String month;

    private Double income;

    public StatisticMonthly(User user, String month, Double income) {
        this.user = user;
        this.month = month;
        this.income = income;
    }

    public StatisticMonthly() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }
}
