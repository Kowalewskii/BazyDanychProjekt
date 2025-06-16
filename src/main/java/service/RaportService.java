package service;

import dao.MovieDAO;
import dao.SubscriptionOrderDAO;
import dao.UserDAO;
import objs.Movie;
import objs.Show;
import objs.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import service.raportsUtil.ActiveSubscriptionByCategoryDTO;
import service.raportsUtil.MovieRentalRevenueByCategoryDTO;
import service.raportsUtil.MoviesCountByCategoryDTO;
import util.HibernateUtil;

import java.util.List;

public class RaportService {
    private final SessionFactory sessionFactory;
    public RaportService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    private SubscriptionOrderDAO subscriptionOrderDAO=new SubscriptionOrderDAO();
    private MovieDAO movieDAO=new MovieDAO();
    private UserDAO userDAO=new UserDAO();
    
    // aktywne subskrypcje wg kategorii
    public List<ActiveSubscriptionByCategoryDTO> activeSubscriptionsByCategory() {
        Session session = sessionFactory.openSession();
        List<ActiveSubscriptionByCategoryDTO> results = session.createQuery("""
        select new service.raportsUtil.ActiveSubscriptionByCategoryDTO(so.subscriptionCategory.name, count(so))
        from SubscriptionOrder so
        where so.paymentStatus = 'PAID'
        group by so.subscriptionCategory.name
        """, ActiveSubscriptionByCategoryDTO.class).getResultList();
        session.close();
        return results;
    }

    public List<Movie> moviesBySubscriptionCategory(String subscriptionCategoryName) {
        Session session = sessionFactory.openSession();
        List<Movie> results = session.createQuery("""
        select m
        from Movie m
        where m.subscriptionCategory.name = :subCatName
        """, Movie.class)
                .setParameter("subCatName", subscriptionCategoryName)
                .getResultList();
        session.close();
        return results;
    }

    public List<Show> showsBySubscriptionCategory(String subscriptionCategoryName) {
        Session session = sessionFactory.openSession();
        List<Show> results = session.createQuery("""
        select s
        from Show s
        where s.subscriptionCategory.name = :subCatName
        """, Show.class)
                .setParameter("subCatName", subscriptionCategoryName)
                .getResultList();
        session.close();
        return results;
    }

    //liczba filmow w kategoriach
    public List<MoviesCountByCategoryDTO> moviesCountByCategory() {
        Session session = sessionFactory.openSession();
        List<MoviesCountByCategoryDTO> results = session.createQuery("""
        select new service.raportsUtil.MoviesCountByCategoryDTO(m.category.categoryName, count(m))
        from Movie m
        group by m.category.categoryName
        """, MoviesCountByCategoryDTO.class).getResultList();
        session.close();
        return results;
    }

    //uzytkownicy bez subskrypcji
    public List<User> usersWithoutActiveSubscription() {
        Session session = sessionFactory.openSession();
        List<User> results = session.createQuery("""
        select u
        from User u
        where not exists (
            select 1 from SubscriptionOrder so 
            where so.user.id = u.id and so.paymentStatus = 'PAID'
        )
        """, User.class).getResultList();
        session.close();
        return results;
    }
    public List<MovieRentalRevenueByCategoryDTO> rentalRevenueByCategory() {
        Session session = sessionFactory.openSession();
        // suma rentPrice grupujac po kategorii
        List<MovieRentalRevenueByCategoryDTO> results = session.createQuery("""
        select new service.raportsUtil.MovieRentalRevenueByCategoryDTO(
            m.category.categoryName, 
            sum(m.rentPrice)
        )
        from MovieOrder mo
        join mo.movie m
        where mo.rented = true
        group by m.category.categoryName
        """, MovieRentalRevenueByCategoryDTO.class)
                .getResultList();

        session.close();
        return results;
    }


}
