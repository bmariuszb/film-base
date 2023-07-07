package zti.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity(name = "Movie")
@Table(name = "movie", schema = "public")
@NamedQuery(name = "getAllMovies", query = "SELECT m FROM Movie m ORDER BY m.title")
@NamedNativeQuery( name = "updateMovie", query = "SELECT update_movie_average_rating(?)", resultClass = Void.class )
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "description")
    private String description;

    @Column(name = "total_rating", columnDefinition = "DECIMAL(12, 2) DEFAULT 0")
    private BigDecimal totalRating;

    @Column(name = "num_ratings", columnDefinition = "INTEGER DEFAULT 0")
    private Integer numRatings;

    public Movie() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(BigDecimal totalRating) {
        this.totalRating = totalRating;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(Integer numRatings) {
        this.numRatings = numRatings;
    }

    public Double getAvgRating() {
        Double avgRating = 0.0;
        if (numRatings > 0) {
            avgRating = getTotalRating().divide(BigDecimal.valueOf(getNumRatings()), 2, RoundingMode.HALF_UP).doubleValue();
        }
        return avgRating;
    }
}
