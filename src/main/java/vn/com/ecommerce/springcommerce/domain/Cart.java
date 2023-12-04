package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cart")
public class Cart extends SelectedList{
    // it's like overriding the fetch type of items from LAZY to EAGER,
    // and overriding the items variable in SelectedList
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SelectedItem> items = new ArrayList<>();
    public Cart() {
    }

    public void addToCart(Product product, int quantity) {
        // check if product already in cart, increase quantity
        for (SelectedItem item : this.items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                this.totalPrice += product.getPrice() * quantity;
                return;
            }
        }
        SelectedItem selectedItem = new SelectedItem(product, quantity, this);
        this.items.add(selectedItem);
        this.totalPrice += product.getPrice() * quantity;
    }

    public SelectedItem getFromCart(Long productId) {
        for (SelectedItem item : this.items) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    public SelectedItem removeFromCart(Long productId) {
        for (SelectedItem item : this.items) {
            if (item.getProduct().getId().equals(productId)) {
                this.items.remove(item);
                this.totalPrice -= item.getTotalPrice();
                return item;
            }
        }
        return null;
    }

    public Double changeQuantity(Long productId, int quantity){
        for (SelectedItem item : this.items) {
            int delta = quantity - item.getQuantity();
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                this.totalPrice += delta * item.getProduct().getPrice();
                return delta * item.getProduct().getPrice();
            }
        }
        return 0D;
    }

    public void clearCart() {
        this.items.clear();
        this.totalPrice = 0D;
    }

    public Order moveCartToOrder() {
        if (this.items.isEmpty()) {
            return null;
        }
        Order order = new Order(this);
        return order;
    }
}