package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUserById(Integer id){
        return userRepository.findUserById(id);
    }

    public User findUserByIdAndRole(Integer id, Integer role){
        return userRepository.findUserByIdAndRole(id, role);
    }


    public User updateUser(User user){
        return userRepository.save(user);
    }
}
