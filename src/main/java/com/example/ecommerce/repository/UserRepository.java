package com.example.ecommerce.repository;

import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);

    public User findUserById(Integer id);

    public User findUserByIdAndRole(Integer id, Integer role);
}
