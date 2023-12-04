package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends AbstractPersistable<Integer> {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "brand")
    private List<Product> products;

    public Brand(String name) {
        this.name = name;
    }
    public String getLogo() {
        return "/images/" + name.toLowerCase() + ".png";
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                '}';
    }
}
