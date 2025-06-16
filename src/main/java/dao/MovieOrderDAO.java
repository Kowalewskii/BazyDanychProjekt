package dao;

import objs.orders.MovieOrder;
import org.hibernate.Session;
import util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

public class MovieOrderDAO implements BasicDAO<MovieOrder> {
    @Override
    public void save(MovieOrder order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(order);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public MovieOrder getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        MovieOrder order = (MovieOrder) session.get(MovieOrder.class, id);
        session.close();
        return order;
    }

    @Override
    public void update(MovieOrder order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(order);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(MovieOrder order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(order);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<MovieOrder> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<MovieOrder> orders = session.createQuery("from MovieOrder", MovieOrder.class).list();
        session.close();
        return orders;
    }

    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM MovieOrder").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public long countRentalsNow(Long movieId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        LocalDateTime cutoff = LocalDateTime.now().minusHours(48);

        Long count = session.createQuery("""
            select count(mo) from MovieOrder mo
            where mo.movie.movieID = :movieId
            and mo.rented = true
            and mo.dateOfOrder >= :cutoff
        """, Long.class)
                .setParameter("movieId", movieId)
                .setParameter("cutoff", cutoff)
                .uniqueResult();

        session.close();
        return count != null ? count : 0;
    }

}
