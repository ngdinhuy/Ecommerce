package com.example.ecommerce.service;

import com.example.ecommerce.model.StatisticMonthly;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.StatisticMonthlyReposotory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticMonthlyService {
    @Autowired
    private StatisticMonthlyReposotory repository;

    public StatisticMonthly addIncome(StatisticMonthly statisticMonthly){
        StatisticMonthly existStat  = repository.findByUserAndMonth(statisticMonthly.getUser(), statisticMonthly.getMonth());
        if (existStat != null){
            existStat.setIncome(existStat.getIncome() + statisticMonthly.getIncome());
            return repository.save(existStat);
        } else {
            return repository.save(statisticMonthly);
        }
    }

    public List<StatisticMonthly> getListStatisticMonthly(User user){
        return repository.findStatisticMonthliesByUser(user);
    }
}
