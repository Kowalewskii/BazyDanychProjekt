package service.raportsUtil;


public class MovieRentalRevenueByCategoryDTO {
    private String categoryName;
    private Long totalRevenue;

    public MovieRentalRevenueByCategoryDTO(String categoryName, Long totalRevenue) {
        this.categoryName = categoryName;
        this.totalRevenue = totalRevenue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getTotalRevenue() {
        return totalRevenue;
    }

    @Override
    public String toString() {
        return "MovieRentalRevenueByCategoryDTO{" +
                "categoryName='" + categoryName + '\'' +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}
