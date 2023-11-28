package org.example.Lab9_10.Repositories;

import org.example.Lab9_10.Domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findByUsername(String username);
}