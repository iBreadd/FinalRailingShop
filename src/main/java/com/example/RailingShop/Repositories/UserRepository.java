package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    List<User> findAllByRole(Role role);
}
