import dao.EpisodeDAO;
import dao.ShowDAO;
import objs.Show;
import objs.ShowEpisode;
import objs.SubscriptionCategory;
import objs.orders.SubscriptionOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ShowEpisodeService;
import service.orders.SubscriptionOrderService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShowEpisodeServiceTest {

    private ShowEpisodeService showEpisodeService;
    private ShowDAO mockShowDao;
    private EpisodeDAO mockEpisodeDao;
    private SubscriptionOrderService mockSubscriptionOrderService;

    @BeforeEach
    public void setUp() {
        mockShowDao = mock(ShowDAO.class);
        mockEpisodeDao = mock(EpisodeDAO.class);
        mockSubscriptionOrderService = mock(SubscriptionOrderService.class);

        showEpisodeService = new ShowEpisodeService();

        showEpisodeService.showDao = mockShowDao;
        showEpisodeService.episodeDao = mockEpisodeDao;
        showEpisodeService.subscriptionOrderService = mockSubscriptionOrderService;
    }

    @Test
    public void addEpisode_success() {
        Show show = new Show();
        show.setShowName("Test Show");
        Long showId = 1L;

        when(mockShowDao.getById(showId)).thenReturn(show);

        showEpisodeService.addEpisode(showId, 1, "Pilot", LocalDate.now(), 45, "/path/to/episode", "Description");

        verify(mockShowDao).getById(showId);
        verify(mockEpisodeDao).save(any(ShowEpisode.class));
    }

    @Test
    public void addEpisode_showNotFound_throws() {
        Long showId = 2L;
        when(mockShowDao.getById(showId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showEpisodeService.addEpisode(showId, 1, "Title", LocalDate.now(), 30, "/path", "desc");
        });

        assertEquals("Show not found", ex.getMessage());
        verify(mockEpisodeDao, never()).save(any());
    }

    @Test
    public void getEpisode_success() {
        ShowEpisode episode = new ShowEpisode();
        episode.setEpisodeTitle("Ep1");
        Long episodeId = 1L;

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);

        ShowEpisode result = showEpisodeService.getEpisode(episodeId);

        assertNotNull(result);
        assertEquals("Ep1", result.getEpisodeTitle());
    }

    @Test
    public void getAllEpisodes_success() {
        ShowEpisode e1 = new ShowEpisode();
        ShowEpisode e2 = new ShowEpisode();

        when(mockEpisodeDao.getAll()).thenReturn(Arrays.asList(e1, e2));

        List<ShowEpisode> episodes = showEpisodeService.getAllEpisodes();

        assertEquals(2, episodes.size());
        verify(mockEpisodeDao).getAll();
    }

    @Test
    public void deleteEpisode_success() {
        ShowEpisode episode = new ShowEpisode();
        Long episodeId = 1L;

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);

        showEpisodeService.deleteEpisode(episodeId);

        verify(mockEpisodeDao).getById(episodeId);
        verify(mockEpisodeDao).delete(episode);
    }

    @Test
    public void deleteEpisode_notFound_throws() {
        Long episodeId = 2L;
        when(mockEpisodeDao.getById(episodeId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showEpisodeService.deleteEpisode(episodeId);
        });

        assertEquals("Episode not found", ex.getMessage());
        verify(mockEpisodeDao, never()).delete(any());
    }

    @Test
    public void updateEpisodeTitle_success() {
        ShowEpisode episode = new ShowEpisode();
        Long episodeId = 1L;

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);

        showEpisodeService.updateEpisodeTitle(episodeId, "New Title");

        assertEquals("New Title", episode.getEpisodeTitle());
        verify(mockEpisodeDao).update(episode);
    }

    @Test
    public void updateEpisodeTitle_notFound_throws() {
        Long episodeId = 2L;
        when(mockEpisodeDao.getById(episodeId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            showEpisodeService.updateEpisodeTitle(episodeId, "Title");
        });

        assertEquals("Episode not found", ex.getMessage());
        verify(mockEpisodeDao, never()).update(any());
    }

    @Test
    public void canAccessEpisode_noEpisode_returnsFalse() {
        Long userId = 1L;
        Long episodeId = 1L;
        when(mockEpisodeDao.getById(episodeId)).thenReturn(null);

        boolean result = showEpisodeService.canAccessEpisode(userId, episodeId);

        assertFalse(result);
    }

    @Test
    public void canAccessEpisode_showNull_returnsFalse() {
        Long userId = 1L;
        Long episodeId = 1L;
        ShowEpisode episode = new ShowEpisode();
        episode.setShow(null);

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);

        boolean result = showEpisodeService.canAccessEpisode(userId, episodeId);

        assertFalse(result);
    }

    @Test
    public void canAccessEpisode_noSubscriptionCategory_requiredCategoryNull_returnsTrue() {
        Long userId = 1L;
        Long episodeId = 1L;

        Show show = new Show();
        show.setSubscriptionCategory(null);

        ShowEpisode episode = new ShowEpisode();
        episode.setShow(show);

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);

        boolean result = showEpisodeService.canAccessEpisode(userId, episodeId);

        assertTrue(result);
        verify(mockSubscriptionOrderService, never()).getActiveSubscription(anyLong());
    }

    @Test
    public void canAccessEpisode_noActiveSubscription_returnsFalse() {
        Long userId = 1L;
        Long episodeId = 1L;

        SubscriptionCategory requiredCategory = new SubscriptionCategory();
        requiredCategory.setSubCategoryId(2L);

        Show show = new Show();
        show.setSubscriptionCategory(requiredCategory);

        ShowEpisode episode = new ShowEpisode();
        episode.setShow(show);

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);
        when(mockSubscriptionOrderService.getActiveSubscription(userId)).thenReturn(null);

        boolean result = showEpisodeService.canAccessEpisode(userId, episodeId);

        assertFalse(result);
    }

    @Test
    public void canAccessEpisode_userSubscriptionCategoryNull_returnsFalse() {
        Long userId = 1L;
        Long episodeId = 1L;

        SubscriptionCategory requiredCategory = new SubscriptionCategory();
        requiredCategory.setSubCategoryId(2L);

        Show show = new Show();
        show.setSubscriptionCategory(requiredCategory);

        ShowEpisode episode = new ShowEpisode();
        episode.setShow(show);

        SubscriptionOrder userSubscription = new SubscriptionOrder();
        userSubscription.setSubscriptionCategory(null);

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);
        when(mockSubscriptionOrderService.getActiveSubscription(userId)).thenReturn(userSubscription);

        boolean result = showEpisodeService.canAccessEpisode(userId, episodeId);

        assertFalse(result);
    }

    @Test
    public void canAccessEpisode_userCategoryIdLowerThanRequired_returnsFalse() {
        Long userId = 1L;
        Long episodeId = 1L;

        SubscriptionCategory requiredCategory = new SubscriptionCategory();
        requiredCategory.setSubCategoryId(3L);

        SubscriptionCategory userCategory = new SubscriptionCategory();
        userCategory.setSubCategoryId(2L);

        Show show = new Show();
        show.setSubscriptionCategory(requiredCategory);

        ShowEpisode episode = new ShowEpisode();
        episode.setShow(show);

        SubscriptionOrder userSubscription = new SubscriptionOrder();
        userSubscription.setSubscriptionCategory(userCategory);

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);
        when(mockSubscriptionOrderService.getActiveSubscription(userId)).thenReturn(userSubscription);

        boolean result = showEpisodeService.canAccessEpisode(userId, episodeId);

        assertFalse(result);
    }

    @Test
    public void canAccessEpisode_userCategoryIdEqualOrHigher_returnsTrue() {
        Long userId = 1L;
        Long episodeId = 1L;

        SubscriptionCategory requiredCategory = new SubscriptionCategory();
        requiredCategory.setSubCategoryId(2L);

        SubscriptionCategory userCategory = new SubscriptionCategory();
        userCategory.setSubCategoryId(3L);

        Show show = new Show();
        show.setSubscriptionCategory(requiredCategory);

        ShowEpisode episode = new ShowEpisode();
        episode.setShow(show);

        SubscriptionOrder userSubscription = new SubscriptionOrder();
        userSubscription.setSubscriptionCategory(userCategory);

        when(mockEpisodeDao.getById(episodeId)).thenReturn(episode);
        when(mockSubscriptionOrderService.getActiveSubscription(userId)).thenReturn(userSubscription);

        boolean result = showEpisodeService.canAccessEpisode(userId, episodeId);

        assertTrue(result);
    }
}
