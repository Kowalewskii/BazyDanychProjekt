package dao;

import objs.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class UserDAO implements BasicDAO<User> {
    @Override
    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }
    @Override
    public User getById(Long id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user =session.get(User.class, id);
        session.close();
        return user;
    }
    @Override
    public void update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }
    @Override
    public List<User> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = session.createQuery("FROM User", User.class).list();
        session.close();
        return users;
    }
    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    public User getUserByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<User> query = session.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        User user=null;
        try{
            user=query.uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }
}
