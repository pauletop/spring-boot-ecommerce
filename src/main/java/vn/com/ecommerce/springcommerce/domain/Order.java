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
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "note")
    private String note;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "status")
    private String status;

    public Order() {
    }

    public Order(SelectedList selectedList) {
        super(selectedList);
        this.orderDate = new Date();

    }

    public void setData(String name, String phone, String address, String note, String paymentMethod, String status) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getOrderDate() {
        return String.format("%tI:%<tM %<tp %<td %<tb, %<tY", orderDate);
    }
}
