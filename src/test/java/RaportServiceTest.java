import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import objs.Movie;
import objs.Show;
import objs.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import service.RaportService;
import service.raportsUtil.*;
import util.HibernateUtil;
import java.util.List;
import java.util.ArrayList;


class RaportServiceTest {

    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockSession;

    private RaportService raportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // ← to zainicjalizuje @Mock
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        raportService = new RaportService(mockSessionFactory);
    }


    @Test
    void activeSubscriptionsByCategory_returnsCorrectList() {
        Query<ActiveSubscriptionByCategoryDTO> mockQuery = mock(Query.class);
        List<ActiveSubscriptionByCategoryDTO> mockResult = List.of(
                new ActiveSubscriptionByCategoryDTO("Basic", 10L),
                new ActiveSubscriptionByCategoryDTO("Premium", 5L)
        );

        when(mockSession.createQuery(anyString(), eq(ActiveSubscriptionByCategoryDTO.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockResult);

        List<ActiveSubscriptionByCategoryDTO> result = raportService.activeSubscriptionsByCategory();

        assertEquals(2, result.size());
        assertEquals("Basic", result.get(0).getCategoryName());
        assertEquals(10L, result.get(0).getActiveSubscriptionsCount());
        verify(mockSession).close();
    }

    @Test
    void moviesBySubscriptionCategory_returnsCorrectMovies() {
        Query<Movie> mockQuery = mock(Query.class);
        List<Movie> mockMovies = List.of(new Movie(), new Movie());

        when(mockSession.createQuery(anyString(), eq(Movie.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockMovies);

        List<Movie> result = raportService.moviesBySubscriptionCategory("Premium");

        assertEquals(2, result.size());
        verify(mockQuery).setParameter("subCatName", "Premium");
        verify(mockSession).close();
    }

    @Test
    void showsBySubscriptionCategory_returnsCorrectShows() {
        Query<Show> mockQuery = mock(Query.class);
        List<Show> mockShows = List.of(new Show());

        when(mockSession.createQuery(anyString(), eq(Show.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockShows);

        List<Show> result = raportService.showsBySubscriptionCategory("Premium");

        assertEquals(1, result.size());
        verify(mockQuery).setParameter("subCatName", "Premium");
        verify(mockSession).close();
    }

    @Test
    void moviesCountByCategory_returnsCorrectDTOs() {
        Query<MoviesCountByCategoryDTO> mockQuery = mock(Query.class);
        List<MoviesCountByCategoryDTO> mockDTOs = List.of(
                new MoviesCountByCategoryDTO("Action", 12L),
                new MoviesCountByCategoryDTO("Comedy", 7L)
        );

        when(mockSession.createQuery(anyString(), eq(MoviesCountByCategoryDTO.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockDTOs);

        List<MoviesCountByCategoryDTO> result = raportService.moviesCountByCategory();

        assertEquals(2, result.size());
        assertEquals("Action", result.get(0).getCategoryName());
        assertEquals(12L, result.get(0).getMoviesCount());
        verify(mockSession).close();
    }

    @Test
    void usersWithoutActiveSubscription_returnsUsers() {
        Query<User> mockQuery = mock(Query.class);
        List<User> mockUsers = List.of(new User(), new User());

        when(mockSession.createQuery(anyString(), eq(User.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockUsers);

        List<User> result = raportService.usersWithoutActiveSubscription();

        assertEquals(2, result.size());
        verify(mockSession).close();
    }

    @Test
    void rentalRevenueByCategory_returnsCorrectDTOs() {
        Query<MovieRentalRevenueByCategoryDTO> mockQuery = mock(Query.class);
        List<MovieRentalRevenueByCategoryDTO> mockDTOs = List.of(
                new MovieRentalRevenueByCategoryDTO("Drama", 15000L),
                new MovieRentalRevenueByCategoryDTO("Thriller", 9000L)
        );

        when(mockSession.createQuery(anyString(), eq(MovieRentalRevenueByCategoryDTO.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockDTOs);

        List<MovieRentalRevenueByCategoryDTO> result = raportService.rentalRevenueByCategory();

        assertEquals(2, result.size());
        assertEquals("Drama", result.get(0).getCategoryName());
        assertEquals(15000L, result.get(0).getTotalRevenue());
        verify(mockSession).close();
    }
}
