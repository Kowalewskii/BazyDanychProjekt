import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dao.CategoryDAO;
import objs.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CategoryService;

public class CategoryServiceTest {

    private CategoryDAO mockDao;
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        mockDao = mock(CategoryDAO.class);
        categoryService = new CategoryService();
        categoryService.dao = mockDao; // wstrzyknięcie mocka
    }

    @Test
    public void testAddCategory_success() {
        String name = "Test Category";

        when(mockDao.getCategoryByName(name)).thenReturn(null);

        categoryService.addCategory(name);

        verify(mockDao).getCategoryByName(name);
        verify(mockDao).save(any(Category.class));
    }

    @Test
    public void testAddCategory_alreadyExists_throws() {
        String name = "Existing Category";
        Category existing = new Category();
        existing.setCategoryName(name);

        when(mockDao.getCategoryByName(name)).thenReturn(existing);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.addCategory(name);
        });

        assertEquals("Category already exists", ex.getMessage());
        verify(mockDao).getCategoryByName(name);
        verify(mockDao, never()).save(any());
    }

    @Test
    public void testGetCategoryById() {
        Long id = 1L;
        Category mockCategory = new Category();
        mockCategory.setCategoryId(id);
        mockCategory.setCategoryName("Mocked");

        when(mockDao.getById(id)).thenReturn(mockCategory);

        Category result = categoryService.getCategoryById(id);

        assertNotNull(result);
        assertEquals(id, result.getCategoryId());
        assertEquals("Mocked", result.getCategoryName());
        verify(mockDao).getById(id);
    }

    @Test
    public void testGetCategoryByName() {
        String name = "Some Category";
        Category mockCategory = new Category();
        mockCategory.setCategoryName(name);

        when(mockDao.getCategoryByName(name)).thenReturn(mockCategory);

        Category result = categoryService.getCategoryByName(name);

        assertNotNull(result);
        assertEquals(name, result.getCategoryName());
        verify(mockDao).getCategoryByName(name);
    }

    @Test
    public void testDeleteCategory_byId() {
        Long id = 2L;
        Category category = new Category();
        category.setCategoryId(id);

        when(mockDao.getById(id)).thenReturn(category);

        categoryService.deleteCategory(id);

        verify(mockDao).getById(id);
        verify(mockDao).delete(category);
    }

    @Test
    public void testDeleteCategory_byName() {
        String name = "ToDelete";
        Category category = new Category();
        category.setCategoryName(name);

        when(mockDao.getCategoryByName(name)).thenReturn(category);

        categoryService.deleteCategory(name);

        verify(mockDao).getCategoryByName(name);
        verify(mockDao).delete(category);
    }
}

