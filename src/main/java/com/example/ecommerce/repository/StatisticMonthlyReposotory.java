package com.example.ecommerce.repository;

import com.example.ecommerce.model.StatisticMonthly;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticMonthlyReposotory extends JpaRepository<StatisticMonthly, Integer> {
    StatisticMonthly findByUserAndMonth(User user, String month);

    List<StatisticMonthly> findStatisticMonthliesByUser(User user);
}
