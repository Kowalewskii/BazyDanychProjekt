package dao;

import objs.ShowEpisode;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class EpisodeDAO implements BasicDAO<ShowEpisode> {

    @Override
    public void save(ShowEpisode episode) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(episode);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public ShowEpisode getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ShowEpisode episode = session.get(ShowEpisode.class, id);
        session.close();
        return episode;
    }

    @Override
    public void update(ShowEpisode episode) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(episode);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(ShowEpisode episode) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(episode);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<ShowEpisode> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ShowEpisode> episodes = session.createQuery("from ShowEpisode ", ShowEpisode.class).list();
        session.close();
        return episodes;
    }


    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM ShowEpisode").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
