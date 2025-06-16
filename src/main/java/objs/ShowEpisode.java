package objs;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "episodes")
public class ShowEpisode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "episode_seq")
    @SequenceGenerator(name = "episode_seq", sequenceName = "episode_seq", allocationSize = 1)
    @Column(name = "episode_id")
    private Long episodeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = false)
    private Show show;

    @Column(name = "episode_number", nullable = false)
    private int episodeNumber;

    @Column(name = "episode_title")
    private String episodeTitle;

    @Column(name = "episode_path")
    private String episodePath;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "description", length = 4000)
    private String description;

    // Gettery i Settery
    public Long getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public String getEpisodePath() {
        return episodePath;
    }

    public void setEpisodePath(String episodePath) {
        this.episodePath = episodePath;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
