package vn.com.ecommerce.springcommerce.domain;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import jakarta.persistence.*;

import java.util.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends AbstractPersistable<Long> {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;
    @ManyToOne
    @JoinColumn(name = "brand_id",
            foreignKey = @ForeignKey(name = "fk_product_brand"))
    private Brand brand;
    @Column(name = "color")
    private String color;
    @Column(name = "imageCount")
    private Integer imageCount = 0;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "fk_product_category"))
    private Category category;
    @Column(name = "stock")
    private Integer stock = 10;
    @Column(name = "sold")
    private Integer sold = 0;
    @Column(name = "rating")
    private Float rating = 0f;
    @Column(name = "rating_count")
    private Integer ratingCount = 0;
    private Date createdAt = new Date();
    @OneToMany(mappedBy = "product")
    private List<CustomerReview> customerReviews;

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, Double price, Integer image) {
        this.name = name;
        this.price = price;
        this.imageCount = image;
    }
    public Product(String name, Double price, String color, String brandName, Category category, int imgCount, String description) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.brand = new Brand(brandName);
        this.imageCount = imgCount;
        this.description = description;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public List<String> getImages() {
        if (imageCount == 0) {
            return null;
        }
        List<String> images = new ArrayList<>(Arrays.asList(new String[imageCount]));
        // eg: /img/12_01.jpg,/img/12_02.jpg,/img/12_03.jpg,/img/12_04.jpg,/img/12_05.jpg
        for (int i = 0; i < imageCount; i++) {
            images.set(i, "/img/" + getId().toString() + "_" + String.format("%02d", i + 1) + ".jpg");
        }
        return images;
    }

    public String getPriceString() {
        return "$" + String.format("%,.2f", price);
    }

    public String getThumbnailFormat() {
        return "/img/thumbnail/"+getId().toString()+".jpg";
    }

    public String getRatingString() {
        if (ratingCount == 0) {
            return "No rating";
        }
        return String.format("%.1f", rating) + " / 5.0";
    }

    public String getRatingHTML() {
        if (ratingCount == 0) {
            return "";
        }
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                html.append("<i class=\"fa fa-star\"></i> ");
            } else {
                html.append("<i class=\"fa fa-star-o\"></i> ");
            }
        }
        return html.toString();
    }
    public String getRatingHTML(Boolean display) {
        if (ratingCount == 0) {
            return "<i class=\"fa fa-star-o\"></i> " +
                    "<i class=\"fa fa-star-o\"></i> " +
                    "<i class=\"fa fa-star-o\"></i> " +
                    "<i class=\"fa fa-star-o\"></i> " +
                    "<i class=\"fa fa-star-o\"></i> ";
        }
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                html.append("<i class=\"fa fa-star\"></i> ");
            } else {
                html.append("<i class=\"fa fa-star-o\"></i> ");
            }
        }
        return html.toString();
    }

    /**
     * Get Base64 encoded link to product
     * @return Base64 encoded JSON string
     */
    public String getLinkBase64() {
        return Base64.getEncoder().encodeToString((getLink()).getBytes());
    }

    /*
    * Get link to product, eg: /laptop/asus/asus-zenbook-ux305ua-ux305ua-fc001t-13-3-inch
    * @return link to product
     */
    //get link with pattern: /{category}/{brand}/{productName}-{productId}
    public String getLink() {
        return "/" + category.getName().toLowerCase() + "/" + brand.getName().toLowerCase() + "/" + name.toLowerCase().replace(" ", "-");
    }

    public String getIdBase64() {
        return Base64.getEncoder().encodeToString((getId().toString()).getBytes());
    }

    public void buyProduct(int quantity) {
        this.stock -= quantity;
        this.sold += quantity;
    }

    public void addRating(int rating) {
        this.rating = (this.rating * this.ratingCount + rating) / (this.ratingCount + 1);
        this.ratingCount++;
    }
}