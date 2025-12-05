package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query("""
       SELECT U FROM User U
       WHERE :username IS NOT NULL 
         AND LOWER(U.username) LIKE LOWER(:username)
       """)
    List<User> searchUsersByUsername(String name);
}
