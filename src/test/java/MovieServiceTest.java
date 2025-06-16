
import dao.CategoryDAO;
import dao.MovieDAO;
import dao.SubscriptionCategoryDAO;
import objs.Category;
import objs.Movie;
import objs.SubscriptionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.MovieService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    private MovieService movieService;
    private MovieDAO mockMovieDao;
    private CategoryDAO mockCategoryDao;
    private SubscriptionCategoryDAO mockSubscriptionCategoryDao;

    @BeforeEach
    public void setUp() {
        mockMovieDao = mock(MovieDAO.class);
        mockCategoryDao = mock(CategoryDAO.class);
        mockSubscriptionCategoryDao = mock(SubscriptionCategoryDAO.class);

        movieService = new MovieService();


        movieService.movieDao = mockMovieDao;
        movieService.categoryDao = mockCategoryDao;
        movieService.subscriptionCategoryDao = mockSubscriptionCategoryDao;
    }

    @Test
    public void testAddMovie_success() {
        // Arrange
        String name = "Movie A";
        String director = "Director A";
        String categoryName = "Action";
        String subscriptionName = "basic";

        Category category = new Category();
        category.setCategoryName(categoryName);

        SubscriptionCategory subscription = new SubscriptionCategory();
        subscription.setName(subscriptionName);
        when(mockMovieDao.getByNameDirector(name, director)).thenReturn(null);
        when(mockCategoryDao.getCategoryByName(categoryName)).thenReturn(category);
        when(mockSubscriptionCategoryDao.getByName(subscriptionName)).thenReturn(subscription);
        // Act
        movieService.addMovie(name, categoryName, 16, true, subscriptionName, 20L,
                "/movie/path", 10L,
                LocalDate.of(2020, 1, 1), director, "description");
        // Assert
        verify(mockMovieDao).getByNameDirector(name, director);
        verify(mockCategoryDao).getCategoryByName(categoryName);
        verify(mockSubscriptionCategoryDao).getByName(subscriptionName);
        verify(mockMovieDao).save(any(Movie.class));
    }

    @Test
    public void testAddMovie_alreadyExists_shouldThrow() {
        String name = "Movie B";
        String director = "Director B";

        when(mockMovieDao.getByNameDirector(name, director)).thenReturn(new Movie());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieService.addMovie(name, "Action", 18, false,
                    null, 15L, "/path", 5L,
                    LocalDate.now(), director, "desc");
        });

        assertEquals("Movie already exists", ex.getMessage());
        verify(mockMovieDao).getByNameDirector(name, director);
        verify(mockMovieDao, never()).save(any());
    }

    @Test
    public void testAddMovie_categoryNotFound_shouldThrow() {
        String name = "Movie C";
        String director = "Director C";
        String categoryName = "Fantasy";

        when(mockMovieDao.getByNameDirector(name, director)).thenReturn(null);
        when(mockCategoryDao.getCategoryByName(categoryName)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieService.addMovie(name, categoryName, 12, false, null,
                    12L, "/path", 3L,
                    LocalDate.now(), director, "desc");
        });

        assertEquals("Category not found", ex.getMessage());
        verify(mockCategoryDao).getCategoryByName(categoryName);
        verify(mockMovieDao, never()).save(any());
    }

    @Test
    public void testAddMovie_subscriptionCategoryNotFound_shouldThrow() {
        String name = "Movie D";
        String director = "Director D";
        String categoryName = "Horror";
        String subscriptionName = "VIP";

        when(mockMovieDao.getByNameDirector(name, director)).thenReturn(null);

        Category category = new Category();
        category.setCategoryName(categoryName);
        when(mockCategoryDao.getCategoryByName(categoryName)).thenReturn(category);

        when(mockSubscriptionCategoryDao.getByName(subscriptionName)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieService.addMovie(name, categoryName, 18, true, subscriptionName,
                    30L, "/path", 8L,
                    LocalDate.now(), director, "desc");
        });

        assertEquals("Subscription category not found", ex.getMessage());
        verify(mockCategoryDao).getCategoryByName(categoryName);
        verify(mockSubscriptionCategoryDao).getByName(subscriptionName);
        verify(mockMovieDao, never()).save(any());
    }

    @Test
    public void testDeleteMovie_byNameDirector_success() {
        String name = "Movie E";
        String director = "Director E";
        Movie mockMovie = new Movie();

        when(mockMovieDao.getByNameDirector(name, director)).thenReturn(mockMovie);

        movieService.deleteMovie(name, director);

        verify(mockMovieDao).getByNameDirector(name, director);
        verify(mockMovieDao).delete(mockMovie);
    }

    @Test
    public void testDeleteMovie_byNameDirector_notFound_shouldThrow() {
        String name = "Movie F";
        String director = "Director F";

        when(mockMovieDao.getByNameDirector(name, director)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                movieService.deleteMovie(name, director));

        assertEquals("Movie not found", ex.getMessage());
        verify(mockMovieDao, never()).delete(any());
    }
    @Test
    public void testGetMovieById_found() {
        Movie movie = new Movie();
        movie.setMovieName("Test Movie");
        when(mockMovieDao.getById(1L)).thenReturn(movie);

        Movie result = movieService.getMovie(1L);
        assertNotNull(result);
        assertEquals("Test Movie", result.getMovieName());
        verify(mockMovieDao).getById(1L);
    }

    @Test
    public void testGetMovieById_notFound() {
        when(mockMovieDao.getById(999L)).thenReturn(null);

        Movie result = movieService.getMovie(999L);
        assertNull(result);
        verify(mockMovieDao).getById(999L);
    }

    @Test
    public void testDeleteMovieById_success() {
        Movie movie = new Movie();
        when(mockMovieDao.getById(5L)).thenReturn(movie);

        movieService.deleteMovie(5L);

        verify(mockMovieDao).getById(5L);
        verify(mockMovieDao).delete(movie);
    }

    @Test
    public void testDeleteMovieById_notFound_shouldThrow() {
        when(mockMovieDao.getById(5L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieService.deleteMovie(5L);
        });

        assertEquals("Movie not found", ex.getMessage());
        verify(mockMovieDao).getById(5L);
        verify(mockMovieDao, never()).delete(any());
    }

    @Test
    public void testGetAllMovies_nonEmptyList() {
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        when(mockMovieDao.getAll()).thenReturn(Arrays.asList(movie1, movie2));

        List<Movie> movies = movieService.getAllMovies();

        assertEquals(2, movies.size());
        verify(mockMovieDao).getAll();
    }

    @Test
    public void testGetAllMovies_emptyList() {
        when(mockMovieDao.getAll()).thenReturn(Collections.emptyList());

        List<Movie> movies = movieService.getAllMovies();

        assertTrue(movies.isEmpty());
        verify(mockMovieDao).getAll();
    }

    @Test
    public void testUpdateMoviePrice_success() {
        Movie movie = new Movie();
        movie.setRentPrice(10L);
        when(mockMovieDao.getByNameDirector("MovieName", "DirectorName")).thenReturn(movie);

        movieService.updateMoviePrice("MovieName", "DirectorName", 25L);

        assertEquals(25L, movie.getRentPrice());
        verify(mockMovieDao).getByNameDirector("MovieName", "DirectorName");
        verify(mockMovieDao).update(movie);
    }

    @Test
    public void testUpdateSubscription_success_withCategory() {
        Movie movie = new Movie();
        movie.setSubscriptionAvailable(false);
        SubscriptionCategory subCat = new SubscriptionCategory();
        subCat.setName("premium");

        when(mockMovieDao.getByNameDirector("MovieName", "DirectorName")).thenReturn(movie);
        when(mockSubscriptionCategoryDao.getByName("premium")).thenReturn(subCat);

        movieService.updateSubscription("MovieName", "DirectorName", true, "premium");

        assertTrue(movie.getSubscriptionAvailable());
        assertEquals("premium", movie.getSubscriptionCategory().getName());
        verify(mockMovieDao).update(movie);
    }

    @Test
    public void testUpdateSubscription_success_withoutCategory() {
        Movie movie = new Movie();
        movie.setSubscriptionAvailable(true);
        movie.setSubscriptionCategory(new SubscriptionCategory());

        when(mockMovieDao.getByNameDirector("MovieName", "DirectorName")).thenReturn(movie);

        movieService.updateSubscription("MovieName", "DirectorName", false, null);

        assertFalse(movie.getSubscriptionAvailable());
        assertNull(movie.getSubscriptionCategory());
        verify(mockMovieDao).update(movie);
    }

    @Test
    public void testUpdateSubscription_subscriptionCategoryNotFound_shouldThrow() {
        Movie movie = new Movie();
        when(mockMovieDao.getByNameDirector("MovieName", "DirectorName")).thenReturn(movie);
        when(mockSubscriptionCategoryDao.getByName("nonexistent")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieService.updateSubscription("MovieName", "DirectorName", true, "nonexistent");
        });

        assertEquals("Subscription category not found", ex.getMessage());
        verify(mockSubscriptionCategoryDao).getByName("nonexistent");
        verify(mockMovieDao, never()).update(any());
    }
}
