package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "order")
public class Order extends SelectedList{
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "address")
    private String address;

    public Order() {
    }

    public Order(SelectedList selectedList) {
        super(selectedList);
        this.orderDate = new Date();
    }
}
