import dao.SubscriptionCategoryDAO;
import objs.SubscriptionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import service.SubscriptionCategoryService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionCategoryServiceTest {

    @Mock
    private SubscriptionCategoryDAO dao;

    @InjectMocks
    private SubscriptionCategoryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSubscriptionCategory_success() {
        String name = "Premium";
        Long price = 100L;
        String description = "Premium subscription";

        when(dao.getByName(name)).thenReturn(null); // no existing category

        service.addSubscriptionCategory(name, price, description);

        ArgumentCaptor<SubscriptionCategory> captor = ArgumentCaptor.forClass(SubscriptionCategory.class);
        verify(dao).save(captor.capture());

        SubscriptionCategory saved = captor.getValue();
        assertEquals(name, saved.getName());
        assertEquals(price, saved.getPrice());
        assertEquals(description, saved.getDescription());
    }

    @Test
    void addSubscriptionCategory_alreadyExists_throws() {
        String name = "Basic";

        when(dao.getByName(name)).thenReturn(new SubscriptionCategory());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.addSubscriptionCategory(name, 50L, "Desc")
        );
        assertTrue(ex.getMessage().contains("already exists"));
        verify(dao, never()).save(any());
    }

    @Test
    void getSubscriptionCategoryById_returnsCategory() {
        SubscriptionCategory cat = new SubscriptionCategory();
        cat.setName("Test");

        when(dao.getById(1L)).thenReturn(cat);

        SubscriptionCategory result = service.getSubscriptionCategory(1L);
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    void getSubscriptionCategoryByName_returnsCategory() {
        SubscriptionCategory cat = new SubscriptionCategory();
        cat.setName("TestName");

        when(dao.getByName("TestName")).thenReturn(cat);

        SubscriptionCategory result = service.getSubscriptionCategory("TestName");
        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void deleteSubscriptionCategoryById_success() {
        SubscriptionCategory cat = new SubscriptionCategory();

        when(dao.getById(1L)).thenReturn(cat);

        service.deleteSubscriptionCategory(1L);

        verify(dao).delete(cat);
    }

    @Test
    void deleteSubscriptionCategoryById_notFound_throws() {
        when(dao.getById(99L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.deleteSubscriptionCategory(99L)
        );
        assertTrue(ex.getMessage().contains("not found"));
        verify(dao, never()).delete(any());
    }

    @Test
    void updateSubscriptionCategory_success() {
        SubscriptionCategory cat = new SubscriptionCategory();
        cat.setName("Gold");

        when(dao.getByName("Gold")).thenReturn(cat);

        service.updateSubscriptionCategory("Gold", 200L, "Updated description");

        assertEquals(200L, cat.getPrice());
        assertEquals("Updated description", cat.getDescription());
        verify(dao).update(cat);
    }

    @Test
    void updateSubscriptionCategory_notFound_throws() {
        when(dao.getByName("Silver")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.updateSubscriptionCategory("Silver", 150L, "Desc")
        );
        assertTrue(ex.getMessage().contains("not found"));
        verify(dao, never()).update(any());
    }

    @Test
    void updateSubscriptionCategoryPrice_success() {
        SubscriptionCategory cat = new SubscriptionCategory();
        cat.setName("Silver");

        when(dao.getByName("Silver")).thenReturn(cat);

        service.updateSubscriptionCategoryPrice("Silver", 300L);

        assertEquals(300L, cat.getPrice());
        verify(dao).update(cat);
    }

    @Test
    void updateSubscriptionCategoryPrice_notFound_throws() {
        when(dao.getByName("Bronze")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.updateSubscriptionCategoryPrice("Bronze", 100L)
        );
        assertTrue(ex.getMessage().contains("not found"));
        verify(dao, never()).update(any());
    }

    @Test
    void updateSubscriptionCategoryDescription_success() {
        SubscriptionCategory cat = new SubscriptionCategory();
        cat.setName("Platinum");

        when(dao.getByName("Platinum")).thenReturn(cat);

        service.updateSubscriptionCategoryDescription("Platinum", "New desc");

        assertEquals("New desc", cat.getDescription());
        verify(dao).update(cat);
    }

    @Test
    void updateSubscriptionCategoryDescription_notFound_throws() {
        when(dao.getByName("Diamond")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.updateSubscriptionCategoryDescription("Diamond", "Desc")
        );
        assertTrue(ex.getMessage().contains("not found"));
        verify(dao, never()).update(any());
    }
}
