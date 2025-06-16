package service;

import dao.CategoryDAO;
import objs.Category;

public class CategoryService {
    public CategoryDAO dao = new CategoryDAO();
    public void addCategory(String name){
        if(dao.getCategoryByName(name) != null){
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = new Category();
        category.setCategoryName(name);
        dao.save(category);
    }
    public Category getCategoryById(Long id){
        return dao.getById(id);
    }
    public Category getCategoryByName(String name){
        return dao.getCategoryByName(name);
    }
    public void deleteCategory(Long id){
        Category category = dao.getById(id);
        dao.delete(category);
    }
    public void deleteCategory(String name){
        Category category=dao.getCategoryByName(name);
        dao.delete(category);
    }
}
