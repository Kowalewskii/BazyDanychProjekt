package objs;
import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_seq")
    @SequenceGenerator(name="category_seq", sequenceName = "category_seq", allocationSize = 1)
    @Column(name="category_id")
    private Long categoryId;

    @Column(name="category_name", nullable = false)
    private String categoryName;

    //SETTERS
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    //GETTERS
    public Long getCategoryId() {
        return categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
}
