package vn.com.ecommerce.springcommerce.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Role;
import vn.com.ecommerce.springcommerce.repository.AccountRepository;
import vn.com.ecommerce.springcommerce.service.AccountService;
import vn.com.ecommerce.springcommerce.service.CartService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> accountRepository.findAccountByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HttpSession session) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
//                                .requestMatchers("/account", "/order/**", "/cart/**").authenticated()
//                                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(customizer -> customizer
                        .loginPage("/account/login").permitAll()
                        .usernameParameter("email")
                        .passwordParameter("pass")
                        .successHandler(((request, response, authentication) -> {
                            Account account = (Account) authentication.getPrincipal();
                            Cart cart = account.getCart();
                            if (cart == null) {
                                cart = new Cart();
                                cart.setAccount(account);
                                account.setCart(cart);
                            }
                            session.setAttribute("isLogin", true);
                            session.setAttribute("accEmail", account.getEmail());
                            session.setAttribute("sCart", cart);
                            if (account.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))) {
                                response.sendRedirect("/admin/products");
                            } else {
                                response.sendRedirect("/");
                            }
                        })))
                .rememberMe(rememberMeConfigurer -> rememberMeConfigurer
                        .rememberMeParameter("remember-me")
                        .key("remember-me")
                        .userDetailsService(userDetailsService())
                        .tokenValiditySeconds(60 * 60 * 24 * 30))
                .logout(customizer -> customizer
                        .logoutUrl("/account/logout")
                        .deleteCookies("JSESSIONID"))
                .httpBasic(HttpBasicConfigurer::disable);
        return http.build();
    }
}