package service.orders;

import dao.SubscriptionOrderDAO;
import dao.SubscriptionCategoryDAO;
import dao.UserDAO;
import objs.orders.SubscriptionOrder;
import objs.SubscriptionCategory;
import objs.User;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SubscriptionOrderService {

    public SubscriptionOrderDAO subscriptionOrderDAO = new SubscriptionOrderDAO();
    public SubscriptionCategoryDAO subscriptionCategoryDAO = new SubscriptionCategoryDAO();
    public UserDAO userDAO = new UserDAO();

    @Transactional
    public void buySubscription(Long userId, Long subscriptionCategoryId) {
        User user = userDAO.getById(userId);
        SubscriptionCategory category = subscriptionCategoryDAO.getById(subscriptionCategoryId);

        if (user == null || category == null) {
            throw new IllegalArgumentException("User or subscription category not found");
        }
        SubscriptionOrder activeSubscription = getActiveSubscription(userId);
        if (activeSubscription != null) {
            throw new IllegalStateException("User already has an active subscription");
        }

        SubscriptionOrder order = new SubscriptionOrder();
        order.setUser(user);
        order.setSubscriptionCategory(category);
        LocalDateTime now = LocalDateTime.now();
        order.setDateOfSubscription(now);
        order.setPaymentStatus("PAID");

        subscriptionOrderDAO.save(order);
    }
    private static final int SUBSCRIPTION_DAYS=30;

    public SubscriptionOrder getActiveSubscription(Long userId) {
        List<SubscriptionOrder> orders = subscriptionOrderDAO.findByUserIdAndPaymentStatus(userId, "PAID");
        LocalDateTime now = LocalDateTime.now();
        return orders.stream()
                .filter(order -> order.getDateOfSubscription().plusDays(SUBSCRIPTION_DAYS).isAfter(now))
                .max(Comparator.comparing(SubscriptionOrder::getDateOfSubscription))
                .orElse(null);

    }
}
