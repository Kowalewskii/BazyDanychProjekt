package dao;

import objs.User;
import objs.orders.SubscriptionOrder;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class SubscriptionOrderDAO implements BasicDAO<SubscriptionOrder> {

    @Override
    public void save(SubscriptionOrder order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(order);
        session.getTransaction().commit();
        session.close();
    }
    @Override
    public SubscriptionOrder getById(Long id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        SubscriptionOrder order =session.get(SubscriptionOrder.class, id);
        session.close();
        return order;
    }
    @Override
    public void update(SubscriptionOrder order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(order);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(SubscriptionOrder order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(order);
        session.getTransaction().commit();
        session.close();
    }
    @Override
    public List<SubscriptionOrder> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SubscriptionOrder> orders = session.createQuery("FROM SubscriptionOrder", SubscriptionOrder.class).list();
        session.close();
        return orders;
    }
    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM SubscriptionOrder").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public List<SubscriptionOrder> findByUserIdAndPaymentStatus(Long userId, String paymentStatus) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SubscriptionOrder> results = session.createQuery("""
        FROM SubscriptionOrder so
        WHERE so.user.id = :userId
        AND so.paymentStatus = :paymentStatus
    """, SubscriptionOrder.class)
                .setParameter("userId", userId)
                .setParameter("paymentStatus", paymentStatus)
                .getResultList();
        session.close();
        return results;
    }

}
