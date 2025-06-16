import dao.SubscriptionOrderDAO;
import dao.SubscriptionCategoryDAO;
import dao.UserDAO;
import objs.SubscriptionCategory;
import objs.User;
import objs.orders.SubscriptionOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.orders.SubscriptionOrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionOrderServiceTest {

    private SubscriptionOrderDAO mockSubscriptionOrderDAO;
    private SubscriptionCategoryDAO mockSubscriptionCategoryDAO;
    private UserDAO mockUserDAO;
    private SubscriptionOrderService subscriptionOrderService;

    @BeforeEach
    public void setUp() {
        mockSubscriptionOrderDAO = mock(SubscriptionOrderDAO.class);
        mockSubscriptionCategoryDAO = mock(SubscriptionCategoryDAO.class);
        mockUserDAO = mock(UserDAO.class);

        subscriptionOrderService = new SubscriptionOrderService();
        subscriptionOrderService.subscriptionOrderDAO = mockSubscriptionOrderDAO;
        subscriptionOrderService.subscriptionCategoryDAO = mockSubscriptionCategoryDAO;
        subscriptionOrderService.userDAO = mockUserDAO;
    }

    @Test
    public void buySubscription_success() {
        Long userId = 1L;
        Long categoryId = 2L;

        User user = new User();
        user.setID(userId);

        SubscriptionCategory category = new SubscriptionCategory();
        category.setSubCategoryId(categoryId);

        when(mockUserDAO.getById(userId)).thenReturn(user);
        when(mockSubscriptionCategoryDAO.getById(categoryId)).thenReturn(category);

        subscriptionOrderService.buySubscription(userId, categoryId);

        verify(mockUserDAO).getById(userId);
        verify(mockSubscriptionCategoryDAO).getById(categoryId);
        verify(mockSubscriptionOrderDAO).save(any(SubscriptionOrder.class));
    }

    @Test
    public void buySubscription_userNotFound_throws() {
        Long userId = 1L;
        Long categoryId = 2L;

        when(mockUserDAO.getById(userId)).thenReturn(null);
        when(mockSubscriptionCategoryDAO.getById(categoryId)).thenReturn(new SubscriptionCategory());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            subscriptionOrderService.buySubscription(userId, categoryId);
        });

        assertEquals("User or subscription category not found", ex.getMessage());
        verify(mockUserDAO).getById(userId);
        verify(mockSubscriptionOrderDAO, never()).save(any());
    }

    @Test
    public void buySubscription_subscriptionCategoryNotFound_throws() {
        Long userId = 1L;
        Long categoryId = 2L;

        when(mockUserDAO.getById(userId)).thenReturn(new User());
        when(mockSubscriptionCategoryDAO.getById(categoryId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            subscriptionOrderService.buySubscription(userId, categoryId);
        });

        assertEquals("User or subscription category not found", ex.getMessage());
        verify(mockUserDAO).getById(userId);
        verify(mockSubscriptionCategoryDAO).getById(categoryId);
        verify(mockSubscriptionOrderDAO, never()).save(any());
    }
    private static final int SUBSCRIPTION_DAYS = 30;

    @Test
    public void getActiveSubscription_shouldReturnActiveSubscription() {
        Long userId = 1L;

        LocalDateTime now = LocalDateTime.now();
        SubscriptionOrder activeOrder = new SubscriptionOrder();
        activeOrder.setDateOfSubscription(now.minusDays(10));  // wciąż aktywna
        activeOrder.setPaymentStatus("PAID");

        SubscriptionOrder oldOrder = new SubscriptionOrder();
        oldOrder.setDateOfSubscription(now.minusDays(40));  // wygasła
        oldOrder.setPaymentStatus("PAID");

        when(mockSubscriptionOrderDAO.findByUserIdAndPaymentStatus(userId, "PAID"))
                .thenReturn(Arrays.asList(oldOrder, activeOrder));

        SubscriptionOrder result = subscriptionOrderService.getActiveSubscription(userId);

        assertNotNull(result);
        assertEquals(activeOrder.getDateOfSubscription(), result.getDateOfSubscription());
        verify(mockSubscriptionOrderDAO).findByUserIdAndPaymentStatus(userId, "PAID");
    }

    @Test
    public void getActiveSubscription_shouldReturnNullWhenNoSubscriptions() {
        Long userId = 1L;

        when(mockSubscriptionOrderDAO.findByUserIdAndPaymentStatus(userId, "PAID"))
                .thenReturn(Collections.emptyList());

        SubscriptionOrder result = subscriptionOrderService.getActiveSubscription(userId);

        assertNull(result);
        verify(mockSubscriptionOrderDAO).findByUserIdAndPaymentStatus(userId, "PAID");
    }

    @Test
    public void getActiveSubscription_shouldReturnNullWhenAllSubscriptionsExpired() {
        Long userId = 1L;

        LocalDateTime now = LocalDateTime.now();
        SubscriptionOrder expiredOrder1 = new SubscriptionOrder();
        expiredOrder1.setDateOfSubscription(now.minusDays(40));
        expiredOrder1.setPaymentStatus("PAID");

        SubscriptionOrder expiredOrder2 = new SubscriptionOrder();
        expiredOrder2.setDateOfSubscription(now.minusDays(31));
        expiredOrder2.setPaymentStatus("PAID");

        when(mockSubscriptionOrderDAO.findByUserIdAndPaymentStatus(userId, "PAID"))
                .thenReturn(Arrays.asList(expiredOrder1, expiredOrder2));

        SubscriptionOrder result = subscriptionOrderService.getActiveSubscription(userId);

        assertNull(result);
        verify(mockSubscriptionOrderDAO).findByUserIdAndPaymentStatus(userId, "PAID");
    }

}
