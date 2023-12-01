package org.example.Lab9_10.Services.User;

import org.example.Lab9_10.Domains.User;
import org.example.Lab9_10.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Configuration
public class UserServiceImplement implements UserService {
    @Autowired
    private UserRepository repo;
    @Override
    public User save(User u) {
        return repo.save(u);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> list = repo.findByUsername(username);
        if (list == null || list.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return list.get(0);
    }
}