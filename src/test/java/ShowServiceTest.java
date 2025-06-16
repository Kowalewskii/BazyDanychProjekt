import dao.CategoryDAO;
import dao.ShowDAO;
import dao.SubscriptionCategoryDAO;
import objs.Category;
import objs.Show;
import objs.SubscriptionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ShowService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShowServiceTest {

    private ShowDAO mockShowDao;
    private CategoryDAO mockCategoryDao;
    private SubscriptionCategoryDAO mockSubscriptionCategoryDao;

    private ShowService showService;

    @BeforeEach
    public void setUp() {
        mockShowDao = mock(ShowDAO.class);
        mockCategoryDao = mock(CategoryDAO.class);
        mockSubscriptionCategoryDao = mock(SubscriptionCategoryDAO.class);

        showService = new ShowService();

        showService.showDao = mockShowDao;
        showService.categoryDao = mockCategoryDao;
        showService.subscriptionCategoryDao = mockSubscriptionCategoryDao;
    }

    @Test
    public void addShow_success() {
        String showName = "Show A";
        String director = "Director A";
        String categoryName = "Comedy";
        String subscriptionCategoryName = "basic";

        when(mockShowDao.getByNameDirector(showName, director)).thenReturn(null);

        Category category = new Category();
        category.setCategoryName(categoryName);
        when(mockCategoryDao.getCategoryByName(categoryName)).thenReturn(category);

        SubscriptionCategory subscriptionCategory = new SubscriptionCategory();
        subscriptionCategory.setName(subscriptionCategoryName);
        when(mockSubscriptionCategoryDao.getByName(subscriptionCategoryName)).thenReturn(subscriptionCategory);

        showService.addShow(showName, categoryName, 10L, 12L, subscriptionCategoryName, "desc", director);

        verify(mockShowDao).getByNameDirector(showName, director);
        verify(mockCategoryDao).getCategoryByName(categoryName);
        verify(mockSubscriptionCategoryDao).getByName(subscriptionCategoryName);
        verify(mockShowDao).save(any(Show.class));
    }

    @Test
    public void addShow_showAlreadyExists_shouldThrow() {
        when(mockShowDao.getByNameDirector("Show B", "Director B")).thenReturn(new Show());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showService.addShow("Show B", "Drama", 5L, 15L,
                    null, "desc", "Director B");
        });

        assertEquals("Show already exists", ex.getMessage());
        verify(mockShowDao).getByNameDirector("Show B", "Director B");
        verify(mockShowDao, never()).save(any());
    }

    @Test
    public void addShow_categoryNotFound_shouldThrow() {
        when(mockShowDao.getByNameDirector("Show C", "Director C")).thenReturn(null);
        when(mockCategoryDao.getCategoryByName("Sci-Fi")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showService.addShow("Show C", "Sci-Fi", 8L, 18L, null, "desc", "Director C");
        });

        assertEquals("Category not found", ex.getMessage());
        verify(mockCategoryDao).getCategoryByName("Sci-Fi");
        verify(mockShowDao, never()).save(any());
    }

    @Test
    public void addShow_subscriptionCategoryNotFound_shouldThrow() {
        when(mockShowDao.getByNameDirector("Show D", "Director D")).thenReturn(null);

        Category category = new Category();
        category.setCategoryName("Thriller");
        when(mockCategoryDao.getCategoryByName("Thriller")).thenReturn(category);

        when(mockSubscriptionCategoryDao.getByName("premium")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showService.addShow("Show D", "Thriller", 12L, 16L, "premium", "desc", "Director D");
        });

        assertEquals("Subscription category not found", ex.getMessage());
        verify(mockSubscriptionCategoryDao).getByName("premium");
        verify(mockShowDao, never()).save(any());
    }

    @Test
    public void getShowById_success() {
        Show show = new Show();
        show.setShowName("Show E");

        when(mockShowDao.getById(1L)).thenReturn(show);

        Show result = showService.getShow(1L);

        assertNotNull(result);
        assertEquals("Show E", result.getShowName());
    }

    @Test
    public void getShowByNameDirector_success() {
        Show show = new Show();
        show.setShowName("Show F");

        when(mockShowDao.getByNameDirector("Show F", "Director F")).thenReturn(show);

        Show result = showService.getShow("Show F", "Director F");

        assertNotNull(result);
        assertEquals("Show F", result.getShowName());
    }

    @Test
    public void getAllShows_success() {
        Show show1 = new Show();
        Show show2 = new Show();

        when(mockShowDao.getAll()).thenReturn(Arrays.asList(show1, show2));

        List<Show> shows = showService.getAllShows();

        assertEquals(2, shows.size());
        verify(mockShowDao).getAll();
    }

    @Test
    public void deleteShowById_success() {
        Show show = new Show();
        when(mockShowDao.getById(1L)).thenReturn(show);

        showService.deleteShow(1L);

        verify(mockShowDao).getById(1L);
        verify(mockShowDao).delete(show);
    }

    @Test
    public void deleteShowById_notFound_shouldThrow() {
        when(mockShowDao.getById(2L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showService.deleteShow(2L);
        });

        assertEquals("Show not found", ex.getMessage());
        verify(mockShowDao).getById(2L);
        verify(mockShowDao, never()).delete(any());
    }

    @Test
    public void deleteShowByNameDirector_success() {
        Show show = new Show();
        when(mockShowDao.getByNameDirector("Show G", "Director G")).thenReturn(show);

        showService.deleteShow("Show G", "Director G");

        verify(mockShowDao).getByNameDirector("Show G", "Director G");
        verify(mockShowDao).delete(show);
    }

    @Test
    public void deleteShowByNameDirector_notFound_shouldThrow() {
        when(mockShowDao.getByNameDirector("Show H", "Director H")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showService.deleteShow("Show H", "Director H");
        });

        assertEquals("Show not found", ex.getMessage());
        verify(mockShowDao).getByNameDirector("Show H", "Director H");
        verify(mockShowDao, never()).delete(any());
    }

    @Test
    public void updateEpNumbers_success() {
        Show show = new Show();
        when(mockShowDao.getById(1L)).thenReturn(show);

        showService.updateEpNumbers(1L, 20L);

        assertEquals(20L, show.getEpNumbers());
        verify(mockShowDao).update(show);
    }

    @Test
    public void updateEpNumbers_showNotFound_shouldThrow() {
        when(mockShowDao.getById(3L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showService.updateEpNumbers(3L, 15L);
        });

        assertEquals("Show not found", ex.getMessage());
        verify(mockShowDao, never()).update(any());
    }

    @Test
    public void updateDirector_success() {
        Show show = new Show();
        when(mockShowDao.getById(1L)).thenReturn(show);

        showService.updateDirector(1L, "New Director");

        assertEquals("New Director", show.getDirector());
        verify(mockShowDao).update(show);
    }

    @Test
    public void updateDirector_showNotFound_shouldThrow() {
        when(mockShowDao.getById(4L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showService.updateDirector(4L, "Someone");
        });

        assertEquals("Show not found", ex.getMessage());
        verify(mockShowDao, never()).update(any());
    }
}
