package vn.com.ecommerce.springcommerce.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account extends AbstractPersistable<Long> implements UserDetails {
    @Column(name = "fullname", nullable = false)
    private String fullname;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
    @Column(name = "status")
    private boolean status = true; // true: active, false: inactive
    @Nullable
    @Column(name = "address")
    private String address;
    @OneToMany(mappedBy = "account")
    private List<Order> orders;
    @Nullable
    @OneToMany(mappedBy = "account")
    private List<Cart> carts;

    public Cart getCart() {
        if (this.carts.isEmpty()) {
            return null;
        }
        return this.carts.get(0);
    }

    public String getPhoneStars() {
        if (this.phone == null) {
            return null;
        }
        return this.phone.replaceAll("(\\d{2})\\d*(\\d{3})", "$1*****$2");
    }

    public void setCart(Cart cart) {
        this.carts.clear();
        this.carts.add(cart);
    }

    public String getIdBase64() {
        return Base64.getEncoder().encodeToString(String.valueOf(this.getId()).getBytes());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((getRole().name())));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Account{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", address='" + address + '\'' +
                '}';
    }
}
