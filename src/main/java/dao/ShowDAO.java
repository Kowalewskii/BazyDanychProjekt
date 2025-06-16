package dao;

import objs.Category;
import objs.Movie;
import objs.Show;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class ShowDAO implements BasicDAO<Show> {
    @Override
    public void save(Show show) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(show);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Show getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Show show = (Show) session.get(Show.class, id);
        session.close();
        return show;
    }

    @Override
    public void update(Show show) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(show);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Show show) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(show);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Show> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Show> shows = session.createQuery("from Show", Show.class).list();
        session.close();
        return shows;
    }
    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Show").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    public Show getByNameDirector(String name, String director) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Show show = null;
        try {
            show = (Show) session.createQuery("from Show where showName = :name AND director= :director", Show.class)
                    .setParameter("name", name)
                    .setParameter("director", director).uniqueResult();
        } finally {
            session.close();
        }
        return show;
    }
}
