import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dao.MovieDAO;
import dao.MovieOrderDAO;
import dao.UserDAO;
import objs.Movie;
import objs.User;
import objs.orders.MovieOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.orders.MovieOrderService;

import java.time.LocalDate;
import java.util.Collections;

public class MovieOrderServiceTest {

    private MovieOrderDAO mockMovieOrderDAO;
    private MovieDAO mockMovieDAO;
    private UserDAO mockUserDAO;
    private MovieOrderService movieOrderService;

    private User user;
    private Movie movie;

    @BeforeEach
    void setUp() {
        mockMovieOrderDAO = mock(MovieOrderDAO.class);
        mockMovieDAO = mock(MovieDAO.class);
        mockUserDAO = mock(UserDAO.class);

        movieOrderService = new MovieOrderService();

        movieOrderService.movieOrderDAO = mockMovieOrderDAO;
        movieOrderService.movieDAO = mockMovieDAO;
        movieOrderService.userDAO = mockUserDAO;

        user = new User();
        user.setID(1L);

        movie = new Movie();
        movie.setMovieID(1L);
        movie.setUserLimit(2L);

        when(mockUserDAO.getById(1L)).thenReturn(user);
        when(mockMovieDAO.getById(1L)).thenReturn(movie);
    }

    @Test
    void rentMovie_success() {
        // 1 wypozyczenie wiec limit 2 nie przekroczony
        when(mockMovieOrderDAO.countRentalsNow(1L)).thenReturn(1L);

        assertDoesNotThrow(() -> movieOrderService.rentMovie(1L, 1L));

        verify(mockUserDAO).getById(1L);
        verify(mockMovieDAO).getById(1L);
        verify(mockMovieOrderDAO).countRentalsNow(1L);
        verify(mockMovieOrderDAO).save(any(MovieOrder.class));
    }

    @Test
    void rentMovie_userNotFound() {
        when(mockUserDAO.getById(2L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieOrderService.rentMovie(2L, 1L);
        });
        assertEquals("user or movie not found", ex.getMessage());

        verify(mockUserDAO).getById(2L);
        verify(mockMovieOrderDAO, never()).save(any());
    }

    @Test
    void rentMovie_movieNotFound() {
        when(mockMovieDAO.getById(2L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieOrderService.rentMovie(1L, 2L);
        });
        assertEquals("user or movie not found", ex.getMessage());

        verify(mockUserDAO).getById(1L);
        verify(mockMovieDAO).getById(2L);
        verify(mockMovieOrderDAO, never()).save(any());
    }

    @Test
    void rentMovie_limitReached() {
        when(mockMovieOrderDAO.countRentalsNow(1L)).thenReturn(2L); // limit = 2, już osiągnięty

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieOrderService.rentMovie(1L, 1L);
        });
        assertEquals("Movie rental limit reached", ex.getMessage());

        verify(mockUserDAO).getById(1L);
        verify(mockMovieDAO).getById(1L);
        verify(mockMovieOrderDAO).countRentalsNow(1L);
        verify(mockMovieOrderDAO, never()).save(any());
    }
}
