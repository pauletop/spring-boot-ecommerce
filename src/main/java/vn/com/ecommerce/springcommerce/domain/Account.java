package vn.com.ecommerce.springcommerce.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import javax.management.relation.Role;
import java.util.Base64;
import java.util.List;

@Data
@Entity
@Table(name = "account")
public class Account extends AbstractPersistable<Long>{
    @Column(name = "fullname", nullable = false)
    private String fullname;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    private String phone;

    @Column
    private String role;
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

    public void setCart(Cart cart) {
        this.carts.clear();
        this.carts.add(cart);
    }

    public String getIdBase64() {
        return Base64.getEncoder().encodeToString(String.valueOf(this.getId()).getBytes());
    }
}
