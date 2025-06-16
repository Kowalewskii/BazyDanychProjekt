package service.orders;

import dao.MovieDAO;
import dao.MovieOrderDAO;
import dao.UserDAO;
import jakarta.transaction.Transactional;
import objs.Movie;
import objs.User;
import objs.orders.MovieOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovieOrderService {

    public MovieOrderDAO movieOrderDAO= new MovieOrderDAO();
    public MovieDAO movieDAO= new MovieDAO();
    public UserDAO userDAO= new UserDAO();

    @Transactional
    public void rentMovie(Long userId,Long movieId) {
        User user = userDAO.getById(userId);
        Movie movie = movieDAO.getById(movieId);

        if(user==null || movie==null) {
            throw new IllegalArgumentException("user or movie not found");
        }

        Long limit=movie.getUserLimit();
        if (limit != null) {
            long rentedNow=movieOrderDAO.countRentalsNow(movieId);
            if (rentedNow>=limit) {
                throw new IllegalArgumentException("Movie rental limit reached");
            }
        }
        MovieOrder order = new MovieOrder();
        order.setUser(user);
        order.setMovie(movie);
        order.setRented(true);
        LocalDateTime now = LocalDateTime.now();
        order.setDateOfOrder(now);

        movieOrderDAO.save(order);
    }
}
