package objs;
import jakarta.persistence.*;

@Entity
@Table(name="series")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "show_seq")
    @SequenceGenerator(name="show_seq",sequenceName = "show_seq", allocationSize = 1)
    @Column(name="series_id")
    private Long showId;

    @Column(name="series_name", nullable = false)
    private String showName;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @Column(name="ep_numbers", nullable = false)
    private Long epNumbers;

    @Column(name="age_restriction")
    private Long ageRestriction;

    @ManyToOne
    @JoinColumn(name="sub_category_id")
    private SubscriptionCategory subscriptionCategory;

    @Column(name="description")
    private String description;

    @Column(name="director")
    private String director;

//    @Column(name="series_path")
//    private String seriesPath;

    //SETTERS
    public void setDirector(String director) {
        this.director = director;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setShowId(Long showId) {
        this.showId = showId;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setEpNumbers(Long epNumbers) {
        this.epNumbers = epNumbers;
    }
    public void setAgeRestriction(Long ageRestriction) {
        this.ageRestriction = ageRestriction;
    }
    public void setSubscriptionCategory(SubscriptionCategory subscriptionCategory) {
        this.subscriptionCategory = subscriptionCategory;
    }
//    public void setSeriesPath(String seriesPath) {
//        this.seriesPath = seriesPath;
//    }
    //GETTERS
    public String getDescription() {
        return description;
    }
    public Long getShowId() {
        return showId;
    }
    public String getShowName() {
        return showName;
    }
    public Category getCategory() {
        return category;
    }
    public Long getEpNumbers() {
        return epNumbers;
    }
    public Long getAgeRestriction() {
        return ageRestriction;
    }
    public SubscriptionCategory getSubscriptionCategory() {
        return subscriptionCategory;
    }
    public String getDirector() {
        return director;
    }
//    public String getSeriesPath() {
//        return seriesPath;
//    }

}
