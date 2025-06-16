package dao;

import objs.Movie;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class MovieDAO implements BasicDAO<Movie> {
    @Override
    public void save(Movie movie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(movie);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Movie getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Movie movie = (Movie) session.get(Movie.class, id);
        session.close();
        return movie;
    }

    @Override
    public void update(Movie movie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(movie);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Movie movie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(movie);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Movie> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
        session.close();
        return movies;
    }


    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Movie").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public Movie getByNameDirector(String name, String director) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Movie movie = null;
        try {
            movie = (Movie) session.createQuery("from Movie where movieName = :name AND director= :director", Movie.class)
                    .setParameter("name", name)
                    .setParameter("director", director).uniqueResult();
        } finally {
            session.close();
        }
        return movie;
    }
}
