package vn.com.ecommerce.springcommerce.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminProduct {
    private String productId;
    private String productName;
    private String productPrice;
    private String productCategory;
    private String productBrand;
    private String productColor;
    private String productStock;
    private String productSold;
    private String productDescription;

    // Getters and setters
}