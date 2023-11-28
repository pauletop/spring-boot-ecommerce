package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "category")
public class Category extends AbstractPersistable<Integer> {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> products;


    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public void setName(String name) { this.name = name; }
}
