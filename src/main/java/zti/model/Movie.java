package zti.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a movie entity.
 */
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

    /**
     * Default constructor.
     */
    public Movie() { }

    /**
     * Retrieves the ID of the movie.
     *
     * @return The ID of the movie.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the movie.
     *
     * @param id The ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the title of the movie.
     *
     * @return The title of the movie.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the author/director of the movie.
     *
     * @return The author/director of the movie.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author/director of the movie.
     *
     * @param author The author/director to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Retrieves the year of release of the movie.
     *
     * @return The year of release of the movie.
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the year of release of the movie.
     *
     * @param year The year to set.
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Retrieves the description of the movie.
     *
     * @return The description of the movie.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the movie.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the total rating accumulated for the movie.
     *
     * @return The total rating of the movie.
     */
    public BigDecimal getTotalRating() {
        return totalRating;
    }

    /**
     * Sets the total rating accumulated for the movie.
     *
     * @param totalRating The total rating to set.
     */
    public void setTotalRating(BigDecimal totalRating) {
        this.totalRating = totalRating;
    }

    /**
     * Retrieves the number of ratings received for the movie.
     *
     * @return The number of ratings received for the movie.
     */
    public Integer getNumRatings() {
        return numRatings;
    }

    /**
     * Sets the number of ratings received for the movie.
     *
     * @param numRatings The number of ratings to set.
     */
    public void setNumRatings(Integer numRatings) {
        this.numRatings = numRatings;
    }

    /**
     * Calculates the average rating of the movie.
     *
     * @return The average rating of the movie.
     */
    public Double getAvgRating() {
        Double avgRating = 0.0;
        if (numRatings > 0) {
            avgRating = getTotalRating().divide(BigDecimal.valueOf(getNumRatings()), 2, RoundingMode.HALF_UP).doubleValue();
        }
        return avgRating;
    }
}
