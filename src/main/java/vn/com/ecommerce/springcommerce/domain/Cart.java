package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Data
@Entity
@Table(name = "cart")
public class Cart extends SelectedList{
    public Cart() {
    }

    public void addToCart(Product product, int quantity) {
        SelectedItem selectedItem = new SelectedItem(product, quantity, this);
        this.items.add(selectedItem);
        this.totalPrice += product.getPrice() * quantity;
    }

    public void removeFromCart(Product product) {
        for (SelectedItem item : this.items) {
            if (item.getProduct().getId().equals(product.getId())) {
                this.items.remove(item);
                this.totalPrice -= item.getProduct().getPrice() * item.getQuantity();
                break;
            }
        }
    }

    public void changeQuantity(Product product, int quantity) {
        for (SelectedItem item : this.items) {
            int delta = quantity - item.getQuantity();
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(quantity);
                this.totalPrice += delta * item.getProduct().getPrice();
                break;
            }
        }
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
        this.clearCart();
        return order;
    }
}