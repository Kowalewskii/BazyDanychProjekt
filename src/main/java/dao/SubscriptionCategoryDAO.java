package dao;

import objs.Category;
import objs.SubscriptionCategory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class SubscriptionCategoryDAO implements BasicDAO<SubscriptionCategory> {
    @Override
    public void save(SubscriptionCategory category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public SubscriptionCategory getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SubscriptionCategory category = (SubscriptionCategory) session.get(SubscriptionCategory.class, id);
        session.close();
        return category;
    }

    @Override
    public void update(SubscriptionCategory category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(SubscriptionCategory category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<SubscriptionCategory> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SubscriptionCategory> categories = session.createQuery("from SubscriptionCategory ", SubscriptionCategory.class).list();
        session.close();
        return categories;
    }
    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM SubscriptionCategory").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    public SubscriptionCategory getByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SubscriptionCategory category = null;
        try {
            Query<SubscriptionCategory> query = session.createQuery(
                    "from SubscriptionCategory where name = :name", SubscriptionCategory.class);
            query.setParameter("name", name);
            category = query.uniqueResult();
        } finally {
            session.close();
        }
        return category;
    }
}
