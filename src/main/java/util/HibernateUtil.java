package util;
import objs.Category;
import objs.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.management.relation.Role;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try{
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(objs.Category.class)
                    .addAnnotatedClass(objs.SubscriptionCategory.class)
                    .addAnnotatedClass(objs.Movie.class)
                    .addAnnotatedClass(objs.Show.class)
                    .addAnnotatedClass(objs.orders.MovieOrder.class)
                    .addAnnotatedClass(objs.orders.SubscriptionOrder.class)
                    .addAnnotatedClass(objs.ShowEpisode.class)
                    .buildSessionFactory();
        }
        catch(Throwable e){
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

