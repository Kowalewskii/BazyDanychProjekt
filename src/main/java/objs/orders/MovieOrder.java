package objs.orders;
import jakarta.persistence.*;
import objs.Movie;
import objs.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name="movies_orders")
public class MovieOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mov_ord_seq")
    @SequenceGenerator(name="mov_ord_seq", sequenceName = "mov_ord_seq", allocationSize = 1)
    @Column(name="movie_order_id")
    private Long OrderId;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(name="rented",nullable = false)
    private boolean rented;

    @Column(name="date_of_order",nullable = false)
    private LocalDateTime dateOfOrder;

    //SETTERS
    public void setOrderId(Long OrderId) {
        this.OrderId = OrderId;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setRented(boolean rented) {
        this.rented = rented;
    }
    public void setDateOfOrder(LocalDateTime dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }
    //GETTERS
    public Long getOrderId() {
        return OrderId;
    }
    public Movie getMovie() {
        return movie;
    }
    public User getUser() {
        return user;
    }
    public boolean isRented() {
        return rented;
    }
    public LocalDateTime getDateOfOrder() {
        return dateOfOrder;
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return this.rented && this.dateOfOrder.plusHours(48).isAfter(now);
    }

}
