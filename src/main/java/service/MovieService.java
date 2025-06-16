package service;

import dao.CategoryDAO;
import dao.MovieDAO;
import dao.SubscriptionCategoryDAO;
import objs.Category;
import objs.Movie;
import objs.SubscriptionCategory;
import objs.User;

import java.time.LocalDate;
import java.util.List;

public class MovieService {
    public MovieDAO movieDao = new MovieDAO();
    public CategoryDAO categoryDao = new CategoryDAO();
    public SubscriptionCategoryDAO subscriptionCategoryDao= new SubscriptionCategoryDAO();

    public void addMovie(String name,
                         String categoryName,
                         int ageRestriction,
                         Boolean subscriptionAvailable,
                         String subscriptionCategory,
                         Long rentPrice,String moviePath,
                         Long userLimit,LocalDate releaseDate,
                         String director, String description){
        if (movieDao.getByNameDirector(name,director)!=null){
            throw new IllegalArgumentException("Movie already exists");
        }
        Category category = categoryDao.getCategoryByName(categoryName);
        if (category==null){
            throw new IllegalArgumentException("Category not found");
        }
        SubscriptionCategory scategory = null;
        if (subscriptionCategory!=null) {
            scategory = subscriptionCategoryDao.getByName(subscriptionCategory);
            if (scategory == null) {
                throw new IllegalArgumentException("Subscription category not found");
            }
        }
        Movie movie = new Movie();
        movie.setMovieName(name);
        movie.setCategory(category);
        movie.setAgeRestriction(ageRestriction);
        movie.setSubscriptionAvailable(subscriptionAvailable);
        movie.setSubscriptionCategory(scategory);
        movie.setRentPrice(rentPrice);
        movie.setMoviePath(moviePath);
        movie.setUserLimit(userLimit);
        movie.setReleaseDate(releaseDate);
        movie.setDirector(director);
        movie.setDescription(description);
        movieDao.save(movie);
    }

    public Movie getMovie(String name, String director){
        return movieDao.getByNameDirector(name,director);
    }
    public Movie getMovie(Long id){
        return movieDao.getById(id);
    }
    public void deleteMovie(Long id){
        Movie movie = movieDao.getById(id);
        if (movie==null){
            throw new IllegalArgumentException("Movie not found");
        }
        movieDao.delete(movie);
    }
    public void deleteMovie(String name,String director){
        Movie movie = movieDao.getByNameDirector(name,director);
        if (movie==null){
            throw new IllegalArgumentException("Movie not found");
        }
        movieDao.delete(movie);
    }
    public List<Movie> getAllMovies(){
        return movieDao.getAll();
    }
    public void updateMoviePrice(String name, String director, Long price){
        Movie movie = getMovie(name, director);
        movie.setRentPrice(price);
        movieDao.update(movie);
    }
    public void updateSubscription(String name, String director,Boolean subscriptionAvailable, String subscriptionCategory){
        Movie movie=getMovie(name,director);
        SubscriptionCategory scategory = null;
        if (subscriptionCategory!=null) {
            scategory = subscriptionCategoryDao.getByName(subscriptionCategory);
            if (scategory == null) {
                throw new IllegalArgumentException("Subscription category not found");
            }
        }
        movie.setSubscriptionAvailable(subscriptionAvailable);
        movie.setSubscriptionCategory(scategory);
        movieDao.update(movie);

    }

}
