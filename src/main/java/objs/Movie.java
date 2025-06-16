package objs;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    @SequenceGenerator(name="movie_seq",sequenceName = "movie_seq", allocationSize = 1)
    @Column(name="movie_id")
    private Long movieID;

    @Column(name="movie_name", nullable=false)
    private String movieName;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @Column(name="age_restriction")
    private int ageRestriction;

    @Column(name="subscription", nullable = false)
    private Boolean subscriptionAvailable;

    @Column(name="rent_price")
    private Long rentPrice;

    @Column(name="movie_path")
    private String moviePath;

    @Column(name="user_limit")
    private Long userLimit;

    @ManyToOne
    @JoinColumn(name="sub_category_id")
    private SubscriptionCategory subscriptionCategory;

    @Column(name="release_date")
    private LocalDate releaseDate;

    @Column(name="director")
    private String director;

    @Column(name="description")
    private String description;

    //SETTERS
    public void setMovieID(Long movieID) {
        this.movieID = movieID;
    }
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setAgeRestriction(int ageRestriction) {
        this.ageRestriction = ageRestriction;
    }
    public void setSubscriptionAvailable(Boolean subscriptionAvailable) {
        this.subscriptionAvailable = subscriptionAvailable;
    }
    public void setRentPrice(Long rentPrice) {
        this.rentPrice = rentPrice;
    }
    public void setMoviePath(String moviePath) {
        this.moviePath = moviePath;
    }
    public void setUserLimit(Long userLimit) {
        this.userLimit = userLimit;
    }
    public void setSubscriptionCategory(SubscriptionCategory subscriptionCategory) {
        this.subscriptionCategory = subscriptionCategory;
    }
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    //GETTERS
    public Long getMovieID() {
        return movieID;
    }
    public String getMovieName() {
        return movieName;
    }
    public Category getCategory() {
        return category;
    }
    public int getAgeRestriction() {
        return ageRestriction;
    }
    public Boolean getSubscriptionAvailable() {
        return subscriptionAvailable;
    }
    public Long getRentPrice() {
        return rentPrice;
    }
    public String getMoviePath() {
        return moviePath;
    }
    public Long getUserLimit() {
        return userLimit;
    }
    public SubscriptionCategory getSubscriptionCategory() {
        return subscriptionCategory;
    }
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public String getDirector() {
        return director;
    }
    public String getDescription() {
        return description;
    }


}
