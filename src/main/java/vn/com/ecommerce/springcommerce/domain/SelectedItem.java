package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    private SelectedList selectedList;


    public SelectedItem(Product product, Integer quantity, SelectedList list) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
        this.selectedList = list;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
    }

    public void setList(SelectedList list) { this.selectedList = list;}

    public String getPriceString() {
        return String.format("%,.2f", this.totalPrice);
    }

    @Override
    public String toString() {
        return "SelectedItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", selectedList=" + selectedList +
                '}';
    }
}