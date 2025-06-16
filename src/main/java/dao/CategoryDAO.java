package dao;

import objs.Category;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class CategoryDAO implements BasicDAO<Category> {
    @Override
    public void save(Category category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Category getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Category category = (Category) session.get(Category.class, id);
        session.close();
        return category;
    }

    @Override
    public void update(Category category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Category category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Category> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Category> categories = session.createQuery("from Category", Category.class).list();
        session.close();
        return categories;
    }

    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Category").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public Category getCategoryByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Category category=null;
        try{
            category = (Category) session.createQuery("from Category where categoryName=:name",
                    Category.class).setParameter("name", name).uniqueResult();
        } finally {
            session.close();
        }
        return category;
    }
}
