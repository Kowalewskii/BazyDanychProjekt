package objs.orders;
import jakarta.persistence.*;
import objs.SubscriptionCategory;
import objs.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="subscriptions_orders")
public class SubscriptionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_ord_seq")
    @SequenceGenerator(name = "sub_ord_seq",sequenceName = "sub_ord_seq", allocationSize = 1)
    @Column(name="subscription_order_id")
    private Long orderId;

    @Column(name="date_of_subscription",nullable=false)
    private LocalDateTime dateOfSubscription;

    @Column(name="payment_status",nullable = false)
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name="subscription_type")
    private SubscriptionCategory subscriptionCategory;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    //SETTERS
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public void setDateOfSubscription(LocalDateTime dateOfSubscription) {
        this.dateOfSubscription = dateOfSubscription;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public void setSubscriptionCategory(SubscriptionCategory subscriptionCategory) {
        this.subscriptionCategory = subscriptionCategory;
    }
    public void setUser(User user) {
        this.user = user;
    }
    //GETTERS
    public Long getOrderId() {
        return orderId;
    }
    public LocalDateTime getDateOfSubscription() {
        return dateOfSubscription;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public SubscriptionCategory getSubscriptionCategory() {
        return subscriptionCategory;
    }
    public User getUser() {
        return user;
    }

}
