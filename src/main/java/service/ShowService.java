package service;

import dao.CategoryDAO;
import dao.ShowDAO;
import dao.SubscriptionCategoryDAO;
import objs.Category;
import objs.Show;
import objs.SubscriptionCategory;

import java.util.List;

public class ShowService {
    public ShowDAO showDao = new ShowDAO();
    public CategoryDAO categoryDao = new CategoryDAO();
    public SubscriptionCategoryDAO subscriptionCategoryDao = new SubscriptionCategoryDAO();

    public void addShow(String showName,
                        String categoryName,
                        Long epNumbers,
                        Long ageRestriction,
                        String subscriptionCategoryName,
                        String description,
                        String director) {

        if (showDao.getByNameDirector(showName, director) != null){
            throw new IllegalArgumentException("Show already exists");
        }

        Category category = categoryDao.getCategoryByName(categoryName);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }

        SubscriptionCategory subscriptionCategory = null;
        if (subscriptionCategoryName != null) {
            subscriptionCategory = subscriptionCategoryDao.getByName(subscriptionCategoryName);
            if (subscriptionCategory == null) {
                throw new IllegalArgumentException("Subscription category not found");
            }
        }

        Show show = new Show();
        show.setShowName(showName);
        show.setCategory(category);
        show.setEpNumbers(epNumbers);
        show.setAgeRestriction(ageRestriction);
        show.setSubscriptionCategory(subscriptionCategory);
        show.setDescription(description);
        show.setDirector(director);

        showDao.save(show);
    }

    public Show getShow(Long id) {
        return showDao.getById(id);
    }
    public Show getShow(String showName, String director) {
        return showDao.getByNameDirector(showName, director);
    }

    public List<Show> getAllShows() {
        return showDao.getAll();
    }

    public void deleteShow(Long id) {
        Show show = showDao.getById(id);
        if (show == null) {
            throw new IllegalArgumentException("Show not found");
        }
        showDao.delete(show);
    }
    public void deleteShow(String showName, String director) {
        Show show = getShow(showName, director);
        if (show == null) {
            throw new IllegalArgumentException("Show not found");
        }
        showDao.delete(show);
    }

    public void updateEpNumbers(Long id, Long epNumbers) {
        Show show = showDao.getById(id);
        if (show == null) {
            throw new IllegalArgumentException("Show not found");
        }
        show.setEpNumbers(epNumbers);
        showDao.update(show);
    }
    public void updateDirector(Long id, String director) {
        Show show = showDao.getById(id);
        if (show == null) {
            throw new IllegalArgumentException("Show not found");
        }
        show.setDirector(director);
        showDao.update(show);
    }
}
