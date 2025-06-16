package service.raportsUtil;

public class ActiveSubscriptionByCategoryDTO {
    private String categoryName;
    private Long activeSubscriptionsCount;

    public ActiveSubscriptionByCategoryDTO(String categoryName, Long activeSubscriptionsCount) {
        this.categoryName = categoryName;
        this.activeSubscriptionsCount = activeSubscriptionsCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getActiveSubscriptionsCount() {
        return activeSubscriptionsCount;
    }

    @Override
    public String toString() {
        return "Category: " + categoryName + ", Active subscriptions: " + activeSubscriptionsCount;
    }
}
