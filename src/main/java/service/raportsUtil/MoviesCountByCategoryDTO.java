package service.raportsUtil;

public class MoviesCountByCategoryDTO {
    private String categoryName;
    private Long moviesCount;

    public MoviesCountByCategoryDTO(String categoryName, Long moviesCount) {
        this.categoryName = categoryName;
        this.moviesCount = moviesCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getMoviesCount() {
        return moviesCount;
    }

    @Override
    public String toString() {
        return "Category: " + categoryName + ", Movies count: " + moviesCount;
    }
}
