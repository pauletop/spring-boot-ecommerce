package org.example.Lab9_10.Services.User;

import org.example.Lab9_10.Domains.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    public User save(User u);
    public User loadUserByUsername(String username) throws UsernameNotFoundException;
}