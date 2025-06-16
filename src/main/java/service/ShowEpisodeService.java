package service;

import dao.EpisodeDAO;
import dao.ShowDAO;
import objs.Show;
import objs.ShowEpisode;
import objs.SubscriptionCategory;
import objs.orders.SubscriptionOrder;
import service.orders.SubscriptionOrderService;

import java.time.LocalDate;
import java.util.List;

public class ShowEpisodeService {
    public SubscriptionOrderService subscriptionOrderService = new SubscriptionOrderService();
    public EpisodeDAO episodeDao = new EpisodeDAO();
    public ShowDAO showDao = new ShowDAO();

    public void addEpisode(Long showId,
                           int episodeNumber,
                           String title,
                           LocalDate releaseDate,
                           Integer duration,
                           String episodePath,
                           String description) {
        Show show = showDao.getById(showId);
        if (show == null) {
            throw new IllegalArgumentException("Show not found");
        }

        ShowEpisode episode = new ShowEpisode();
        episode.setShow(show);
        episode.setEpisodeNumber(episodeNumber);
        episode.setEpisodeTitle(title);
        episode.setReleaseDate(releaseDate);
        episode.setEpisodePath(episodePath);
        episode.setDescription(description);
        episode.setDurationMinutes(duration);

        episodeDao.save(episode);
    }

    public ShowEpisode getEpisode(Long id) {
        return episodeDao.getById(id);
    }

    public List<ShowEpisode> getAllEpisodes() {
        return episodeDao.getAll();
    }

    public void deleteEpisode(Long id) {
        ShowEpisode episode = episodeDao.getById(id);
        if (episode == null) {
            throw new IllegalArgumentException("Episode not found");
        }
        episodeDao.delete(episode);
    }

    public void updateEpisodeTitle(Long id, String newTitle) {
        ShowEpisode episode = episodeDao.getById(id);
        if (episode == null) {
            throw new IllegalArgumentException("Episode not found");
        }
        episode.setEpisodeTitle(newTitle);
        episodeDao.update(episode);
    }
    public void updateEpisode(Long id, String newTitle) {
        ShowEpisode episode = episodeDao.getById(id);
        if (episode == null) {
            throw new IllegalArgumentException("Episode not found");
        }
        episode.setEpisodeTitle(newTitle);
        episodeDao.update(episode);
    }
    public boolean canAccessEpisode(Long userId, Long episodeId) {
        ShowEpisode episode = episodeDao.getById(episodeId);
        if (episode == null) {
            return false;
        }
        Show show=episode.getShow();
        if (show == null || episode==null) {
            return false;
        }
        SubscriptionCategory requiredCategory = show.getSubscriptionCategory();
        if (requiredCategory == null) {
            return true;
        }
        SubscriptionOrder sub = subscriptionOrderService.getActiveSubscription(userId);
        if(sub == null) {
            return false;
        }
        SubscriptionCategory userCategory= sub.getSubscriptionCategory();
        if(userCategory == null) {
            return false;
        }
        return userCategory.getSubCategoryId()>=requiredCategory.getSubCategoryId();

    }
}
