package vn.com.ecommerce.springcommerce.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Data
@Entity
@IdClass(CustomerReviewId.class)
public class CustomerReview {
    @Id
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id",
            nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_CUSTOMER_REVIEW_ACCOUNT"))
    private Account account;
    @Column(name = "display_name")
    private String displayName;
    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id",
            nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_CUSTOMER_REVIEW_PRODUCT"))
    private Product product;
    @Column(name = "rating", nullable = false)
    // @Min(1) @Max(5)
    private Byte rating;
    @Lob
    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public CustomerReview() {
        this.createdAt = new Date();
    }

    public CustomerReview setData(Account account, String displayName, Product product, Byte rating, String comment) {
        this.account = account;
        this.displayName = displayName;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
        return this;
    }

    public String getStars() {
        String stars = "";
        for (int i = 0; i < rating; i++) {
            stars += "<i class=\"fa fa-star\"></i>";
        }
        for (int i = 0; i < 5 - rating; i++) {
            stars += "<i class=\"fa fa-star-o empty\"></i>";
        }
        return stars;
    }

    // get date with format: 27 DEC 2018, 8:00 PM
    public String getCreatedAtString() {
        return (new SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.US)).format(createdAt);
    }


}
@Getter
@Setter
class CustomerReviewId implements Serializable {
    private Account account;
    private Product product;
}
