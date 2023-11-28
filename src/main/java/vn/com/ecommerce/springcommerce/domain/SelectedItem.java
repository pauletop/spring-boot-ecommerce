package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
@Table(name = "selected_item")
public class SelectedItem extends AbstractPersistable<Long> {
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id",
            nullable = false, foreignKey = @ForeignKey(name = "fk_selected_item_product"))
    private Product product;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "total_price")
    private Double totalPrice;
    @ManyToOne
    @JoinColumn(name = "selected_list_id", referencedColumnName = "id",
            nullable = false, foreignKey = @ForeignKey(name = "fk_selected_item_selected_list"))
    private SelectedList list;

    public SelectedItem() {
    }

    public SelectedItem(Product product, Integer quantity, SelectedList list) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
        this.list = list;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
    }

    public void setList(SelectedList list) {
        this.list = list;
    }
}