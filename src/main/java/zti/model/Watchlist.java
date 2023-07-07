package zti.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "Watchlist")
@Table(name = "watchlist", schema = "public")
@NamedQuery(name = "getAllWatchlist", query = "SELECT u FROM Watchlist u WHERE u.rating IS NULL AND u.user.id = :userId")
@NamedQuery(name = "getAllRating", query = "SELECT u FROM Watchlist u WHERE u.rating is not null AND u.user.id = :userId")
@NamedQuery(name = "Watchlist.deleteByUserAndMovie", query = "DELETE FROM Watchlist w WHERE w.user = :user AND w.movie = :movie")
@NamedQuery(name = "selectByUserAndMovie", query = "SELECT w FROM Watchlist w WHERE w.user = :user AND w.movie = :movie")
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

    public Watchlist() {
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
