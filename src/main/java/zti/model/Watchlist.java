package zti.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents a watchlist entity.
 */
@Entity(name = "Watchlist")
@Table(name = "watchlist", schema = "public")
@NamedQuery(name = "getAllWatchlist", query = "SELECT u FROM Watchlist u WHERE u.rating IS NULL AND u.user.id = :userId")
@NamedQuery(name = "getAllRating", query = "SELECT u FROM Watchlist u WHERE u.rating is not null AND u.user.id = :userId")
@NamedQuery(name = "Watchlist.deleteByUserAndMovie", query = "DELETE FROM Watchlist w WHERE w.user = :user AND w.movie = :movie")
@NamedQuery(name = "selectByUserAndMovie", query = "SELECT w FROM Watchlist w WHERE w.user = :user AND w.movie = :movie")
@NamedQuery(name = "Watchlist.exists", query = "SELECT COUNT(w) FROM Watchlist w WHERE w.user.id = :userId AND w.movie.id = :movieId")
public class Watchlist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @Column(name = "rating", columnDefinition = "DECIMAL(2, 0)")
    private BigDecimal rating;

    /**
     * Default constructor.
     */
    public Watchlist() {
    }

    /**
     * Retrieves the user associated with the watchlist item.
     *
     * @return The user associated with the watchlist item.
     */
    public Users getUser() {
        return user;
    }

    /**
     * Sets the user associated with the watchlist item.
     *
     * @param user The user to set.
     */
    public void setUser(Users user) {
        this.user = user;
    }

    /**
     * Retrieves the movie associated with the watchlist item.
     *
     * @return The movie associated with the watchlist item.
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets the movie associated with the watchlist item.
     *
     * @param movie The movie to set.
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Retrieves the rating assigned to the watchlist item.
     *
     * @return The rating of the watchlist item.
     */
    public BigDecimal getRating() {
        return rating;
    }

    /**
     * Sets the rating for the watchlist item.
     *
     * @param rating The rating to set.
     */
    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    /**
     * Retrieves the ID of the watchlist item.
     *
     * @return The ID of the watchlist item.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the watchlist item.
     *
     * @param id The ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
