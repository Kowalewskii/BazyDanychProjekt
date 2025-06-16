package service;

import dao.SubscriptionCategoryDAO;
import objs.SubscriptionCategory;

public class SubscriptionCategoryService {
    public SubscriptionCategoryDAO dao=new SubscriptionCategoryDAO();
    public void addSubscriptionCategory(String name, Long price, String description) {
        if (dao.getByName(name) != null) {
            throw new IllegalArgumentException("Subscription category with name '" + name + "' already exists.");
        }
        SubscriptionCategory subscriptionCategory=new SubscriptionCategory();
        subscriptionCategory.setName(name);
        subscriptionCategory.setPrice(price);
        subscriptionCategory.setDescription(description);
        dao.save(subscriptionCategory);
    }
    public SubscriptionCategory getSubscriptionCategory(Long id) {
        SubscriptionCategory subscriptionCategory=dao.getById(id);
        return subscriptionCategory;
    }
    public SubscriptionCategory getSubscriptionCategory(String name) {
        SubscriptionCategory subscriptionCategory=dao.getByName(name);
        return subscriptionCategory;
    }
    public void deleteSubscriptionCategory(Long id) {
        SubscriptionCategory subscriptionCategory=dao.getById(id);
        if (subscriptionCategory == null) {
            throw new IllegalArgumentException("Subscription not found");
        }
        dao.delete(subscriptionCategory);

    }
    public void deleteSubscriptionCategory(String name) {
        SubscriptionCategory subscriptionCategory=dao.getByName(name);
        if (subscriptionCategory == null) {
            throw new IllegalArgumentException("Subscription not found");
        }
        dao.delete(subscriptionCategory);
    }
    public void updateSubscriptionCategory(String name, Long price, String description) {
        SubscriptionCategory subscriptionCategory=getSubscriptionCategory(name);
        if (subscriptionCategory == null) {
            throw new IllegalArgumentException("Subscription not found");
        }
        subscriptionCategory.setPrice(price);
        subscriptionCategory.setDescription(description);
        dao.update(subscriptionCategory);

    }
    public void updateSubscriptionCategoryPrice(String name, Long price) {
        SubscriptionCategory subscriptionCategory=getSubscriptionCategory(name);
        if (subscriptionCategory == null) {
            throw new IllegalArgumentException("Subscription not found");
        }
        subscriptionCategory.setPrice(price);
        dao.update(subscriptionCategory);
    }
    public void updateSubscriptionCategoryDescription(String name, String description) {
        SubscriptionCategory subscriptionCategory=getSubscriptionCategory(name);
        if (subscriptionCategory == null) {
            throw new IllegalArgumentException("Subscription not found");
        }
        subscriptionCategory.setDescription(description);
        dao.update(subscriptionCategory);
    }
}
