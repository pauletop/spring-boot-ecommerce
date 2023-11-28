package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "selected_list")
public class SelectedList extends AbstractPersistable<Long> {
    @Column(name = "total_price")
    protected Double totalPrice = 0.0;
    @Getter
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    protected List<SelectedItem> items = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id",
            nullable = false, foreignKey = @ForeignKey(name = "fk_selected_list_account"))
    private Account account;

    public SelectedList() {
    }

    public SelectedList(Account account) {
        this.account = account;
    }

    protected SelectedList(SelectedList selectedList){
        this.totalPrice = selectedList.getTotalPrice();
        this.items = selectedList.getItems();
        this.account = selectedList.getAccount();
    }

    // getProducts() method
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        for (SelectedItem item : this.items) {
            products.add(item.getProduct());
        }
        return products;
    }
}
