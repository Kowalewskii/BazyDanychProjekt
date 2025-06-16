import dao.*;
import objs.Movie;
import objs.Show;
import objs.User;
import org.hibernate.SessionFactory;
import service.*;
import service.orders.MovieOrderService;
import service.orders.SubscriptionOrderService;
import service.raportsUtil.ActiveSubscriptionByCategoryDTO;
import service.raportsUtil.MovieRentalRevenueByCategoryDTO;
import service.raportsUtil.MoviesCountByCategoryDTO;
import util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
        RaportService  raportService= new RaportService(sessionFactory);
        try {
            List<ActiveSubscriptionByCategoryDTO> activeSubs = raportService.activeSubscriptionsByCategory();
            System.out.println("=== Aktywne subskrypcje wg kategorii ===");
            activeSubs.forEach(dto ->
                    System.out.println(dto.getCategoryName() + ": " + dto.getActiveSubscriptionsCount())
            );
            List<Movie> movies = raportService.moviesBySubscriptionCategory("basic");
            System.out.println("\n=== Filmy dla kategorii 'basic' ===");
            movies.forEach(m -> System.out.println(m.getMovieName()));

            List<Show> shows = raportService.showsBySubscriptionCategory("premium");
            System.out.println("\n=== Show dla kategorii 'premium' ===");
            shows.forEach(s -> System.out.println(s.getShowName()));

            List<MoviesCountByCategoryDTO> movieCounts = raportService.moviesCountByCategory();
            System.out.println("\n=== Liczba filmów wg kategorii ===");
            movieCounts.forEach(dto ->
                    System.out.println(dto.getCategoryName() + ": " + dto.getMoviesCount())
            );
            List<User> usersNoSub = raportService.usersWithoutActiveSubscription();
            System.out.println("\n=== Użytkownicy bez aktywnej subskrypcji ===");
            usersNoSub.forEach(u -> System.out.println(u.getEmail()));

            List<MovieRentalRevenueByCategoryDTO> revenues = raportService.rentalRevenueByCategory();
            System.out.println("\n=== Przychody z wypożyczeń filmów wg kategorii ===");
            revenues.forEach(dto ->
                    System.out.println(dto.getCategoryName() + ": " + dto.getTotalRevenue())
            );
        } finally {
            sessionFactory.close();
        }
//        SubscriptionOrderService subscriptionOrderService = new SubscriptionOrderService();
//        try {
//            subscriptionOrderService.buySubscription(3L, 1L);
//            System.out.println("Subskrypcja zakupiona pomyślnie");
//        } catch (IllegalArgumentException e) {
//            System.err.println("Błąd przy zakupie subskrypcji: " + e.getMessage());
//        }
//        try {
//            subscriptionOrderService.buySubscription(5L, 3L);
//            System.out.println("Subskrypcja zakupiona pomyślnie");
//        } catch (IllegalArgumentException e) {
//            System.err.println("Błąd przy zakupie subskrypcji: " + e.getMessage());
//        }
//        try {
//            subscriptionOrderService.buySubscription(1L, 2L);
//            System.out.println("Subskrypcja zakupiona pomyślnie");
//        } catch (IllegalArgumentException e) {
//            System.err.println("Błąd przy zakupie subskrypcji: " + e.getMessage());
//        }
//        MovieOrderService movieOrderService = new MovieOrderService();
//        try {
//            movieOrderService.rentMovie(1L, 3L);
//            System.out.println("Film wypożyczony pomyślnie");
//        } catch (IllegalArgumentException e) {
//            System.err.println("Błąd przy wypożyczeniu filmu: " + e.getMessage());
//        }
//        try {
//            movieOrderService.rentMovie(2L, 3L);
//            System.out.println("Film wypożyczony pomyślnie");
//        } catch (IllegalArgumentException e) {
//            System.err.println("Błąd przy wypożyczeniu filmu: " + e.getMessage());
//        }
//        try {
//            movieOrderService.rentMovie(4L, 5L);
//            System.out.println("Film wypożyczony pomyślnie");
//        } catch (IllegalArgumentException e) {
//            System.err.println("Błąd przy wypożyczeniu filmu: " + e.getMessage());
//        }
//
////        ADDING EPISODES
//        ShowEpisodeService showEpisodeService = new ShowEpisodeService();
//        showEpisodeService.addEpisode(1L,1,"Tom and Jerry: Barnyard Bunk",LocalDate.of(1930,1,1),
//                5,"shows/Tom_and_Jerry_Barnyard_Bunk_1932_512kb.mp4","Ep1 adventure");
//        showEpisodeService.addEpisode(1L,2,"Tom and Jerry: Jolly Fish",LocalDate.of(1930,1,1),
//                5,"shows/Tom_and_Jerry_Jolly_Fish_1932_512kb.mp4","Ep2 adventure");
//
//        //ADDING SHOWS
//        ShowService showService = new ShowService();
//        showService.addShow("Tom and Jerry","Comedy",2L,7L,"basic",
//                "Our beloved Tom's and Jerry's take part in various adventures!!","John Doe");
//        //ADDING MOVIES
//        MovieService movieService = new MovieService();
//        movieService.addMovie("The Phantom Of The Opera", "Horror",16,
//                true,"premium",5L,"movies/ThePhantomOfTheOperaPresentedByMoviePowder_512kb.mp4"
//        ,null,LocalDate.of(1925,1,1),"Julian Rupert","At the Opera of Paris, a mysterious phantom " +
//                        "threatens a famous lyric singer, Carlotta and thus forces her to give up her role (Marguerite in Faust) for unknown Christine Daae. Christine meets " +
//                        "this phantom (a masked man) in the catacombs, where he lives.");
//        movieService.addMovie("Peter Pan","Fantasy",7,false,null,5L,
//                "movies/Peter Pan.mp4",2L,LocalDate.of(1924,1,1),
//                "Herbert Brenon", "Peter Pan is a 1924 American silent fantasy adventure film released by Paramount Pictures The film inspired Walt Disney to create his company's 1953 animated adaptation.");
//        movieService.addMovie("Number Please?", "Comedy",12,
//                true,"basic",5L,"movies/Number_Please_512kb.mp4",
//                null,LocalDate.of(1920,1,1),"Hal Roach", "Number, Please? is a 1920 American short comedy film directed by Hal Roach and Fred C. Newmeyer featuring Harold Lloyd.");
//        // ADDING SUBSCRIPTION CATEGORIES
//        SubscriptionCategoryService subscriptionCategoryService = new SubscriptionCategoryService();
//        subscriptionCategoryService.addSubscriptionCategory("basic",10L,"basic subscription, hd, only movies available");
//        subscriptionCategoryService.addSubscriptionCategory("premium", 15L, "allows you to watch shows and movies in full hd");
//        subscriptionCategoryService.addSubscriptionCategory("VIP",50L,"allows you to watch shows and movies in full hd 4k, maximum experience, monthly vip perks package");
//        // ADDING CATEGORIES
//        CategoryService categoryService = new CategoryService();
//        categoryService.addCategory("Romance");
//        categoryService.addCategory("Fantasy");
//        categoryService.addCategory("Horror");
//        categoryService.addCategory("Action");
//        categoryService.addCategory("Comedy");
//        // ADDING USERS
//        UserService userService = new UserService();
//        userService.addUser("Robert", "Lewandowski","+48999999999","probierz@hate.club",
//                LocalDate.of(1980,1,1));
//        userService.addUser("Krzysztof","Gonciarz","+48122112212", "naruciak@jestglupi.pl",
//                LocalDate.of(1990,11,4));
//        userService.addUser("Finn", "Człowiek","+48500855008","pora@naprzygo.de",
//                LocalDate.of(2005,5,14));
//        userService.addUser("Pies", "Jake", "+48123456789","panna@jednorozek.love",
//                LocalDate.of(2010,7,19));
//        userService.addUser("Lodowy","Król","+48666666666","szymonkrol@lodziarnia.ice",
//                LocalDate.of(1000,1,5));
//
//





        System.out.println("Done");
    }
}
