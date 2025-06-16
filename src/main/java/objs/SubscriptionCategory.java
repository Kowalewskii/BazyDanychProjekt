package objs;

import jakarta.persistence.*;

@Entity
@Table(name="subscriptions_categories")
public class SubscriptionCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_category_seq")
    @SequenceGenerator(name="subscription_category_seq",sequenceName = "subscription_category_seq", allocationSize = 1)
    @Column(name="sub_category_id")
    private Long subCategoryId;

    @Column(name="name",nullable=false)
    private String name;

    @Column(name="price",nullable=false)
    private Long price;

    @Column(name="description")
    private String description;

    // SETTERS
    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(Long price) {
        this.price = price;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // GETTERS
    public Long getSubCategoryId() {
        return subCategoryId;
    }
    public String getName() {
        return name;
    }
    public Long getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }



}