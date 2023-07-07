package zti.filmbase;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import zti.model.Movie;
import zti.model.Users;
import zti.model.Watchlist;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

@Path("/jpa2")
public class JPAResource2 {
    private EntityManagerFactory managerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    @Context
    private HttpServletRequest request;
    private StringBuilder htmlBuilder;
    public JPAResource2() {
        managerFactory = createEntityManagerFactory("PU_Postgresql");
        entityManager = managerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();

        htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { background-color: #111111; color: white;\n");
        htmlBuilder.append("  height: 100vh;\n");
        htmlBuilder.append("}\n");
        htmlBuilder.append("button style='background-color: #013220; color: white;\n");
        htmlBuilder.append("a:link { color: yellow; }\n");
        htmlBuilder.append("a:visited { color: orange; }\n");
        htmlBuilder.append("form {\n");
        htmlBuilder.append("  align-items: center;\n");
        htmlBuilder.append("  text-align: center;\n");
        htmlBuilder.append("}\n");
        htmlBuilder.append("table {\n");
        htmlBuilder.append("    width: 100%;\n");
        htmlBuilder.append("    border-collapse: collapse;\n");
        htmlBuilder.append("}\n");
        htmlBuilder.append("th, td {\n");
        htmlBuilder.append("    border: 1px solid white;\n");
        htmlBuilder.append("    padding: 8px;\n");
        htmlBuilder.append("    text-align: left;\n");
        htmlBuilder.append("}\n");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<body>");
        htmlBuilder.append("<div style='text-align: center;'>");
    }


    /**
     *
     * @return
     */
    @GET
    @Path("/movies")
    @Produces({MediaType.TEXT_HTML})
    public String getMovies() {
        String name = (String) request.getSession().getAttribute("username");
        if (name != null) {
            htmlBuilder.append("<p1>Hello " + name + " </p1>");
            htmlBuilder.append("<a href='watchlist'><button style='background-color: #013220; color: white;'>Watchlist</button></a>");
            htmlBuilder.append("<a href='rating'><button style='background-color: #013220; color: white;'>Rating</button></a>");
            htmlBuilder.append("<a href='logout'><button style='background-color: #013220; color: white;'>Logout</button></a>");
        } else {
            htmlBuilder.append("<a href='login'><button style='background-color: #013220; color: white;'>Login</button></a>");
            htmlBuilder.append("<a href='register'><button style='background-color: #013220; color: white;'>Register</button></a>");
        }

        try {
            List<Movie> movies = entityManager.createNamedQuery("getAllMovies", Movie.class).getResultList();
            htmlBuilder.append("<h1>Movies</h1>");
            htmlBuilder.append("<table>");
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<th>Title</th><th>Director</th><th>Year</th><th>Description</th><th>Average rating</th>");

            List<Watchlist> watchlistItems = null;
            List<Watchlist> ratingItems = null;
            if (name != null) {
                TypedQuery<Watchlist> watchlistQuery = entityManager.createNamedQuery("getAllWatchlist", Watchlist.class);
                Integer userId = (Integer) request.getSession().getAttribute("user_id");
                watchlistQuery.setParameter("userId", userId);
                watchlistItems = watchlistQuery.getResultList();
                TypedQuery<Watchlist> ratingQuery = entityManager.createNamedQuery("getAllRating", Watchlist.class);
                ratingQuery.setParameter("userId", userId);
                ratingItems = ratingQuery.getResultList();
                htmlBuilder.append("<th>Watchlist</th><th>Your rate</th>");
            }
            htmlBuilder.append("</tr>");
            for (Movie movie : movies) {
                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(movie.getTitle()).append("</td>");
                htmlBuilder.append("<td>").append(movie.getAuthor()).append("</td>");
                htmlBuilder.append("<td>").append(movie.getYear()).append("</td>");
                htmlBuilder.append("<td>").append(movie.getDescription()).append("</td>");
                if (movie.getAvgRating().intValue() == 0) {
                    htmlBuilder.append("<td>-</td>");
                } else {
                    htmlBuilder.append("<td>").append(movie.getAvgRating()).append("</td>");
                }
                if (name != null) {
                    htmlBuilder.append("<td>");
                    boolean rated = false;
                    Optional<Watchlist> ratedMovie = ratingItems.stream().filter(rating -> rating.getMovie().equals(movie)).findAny();
                    if (!watchlistItems.stream().filter(watchlist -> watchlist.getMovie().equals(movie)).findAny().isEmpty()) {
                        htmlBuilder.append("<form action=\"deleteMovieFromWatchlist\" method=\"POST\">")
                                .append("<input type=\"hidden\" name=\"id\" value=\"").append(movie.getId()).append("\">")
                                .append("<input type=\"hidden\" name=\"path\" value=\"/movies\">")
                                .append("<button type=\"submit\" style=\"background-color: red;\">Remove</button>")
                                .append("</form>");
                    } else if (!ratedMovie.isEmpty()) {
                        rated = true;
                        htmlBuilder .append("<button type=\"submit\" style=\"background-color: gray; width:91;\" disabled>Movie rated</button>");
                    } else {
                        htmlBuilder.append("<form action=\"addMovieToWatchlist\" method=\"POST\">")
                                .append("<input type=\"hidden\" name=\"id\" value=\"").append(movie.getId()).append("\">")
                                .append("<button type=\"submit\" style=\"background-color: green;\">Add</button>")
                                .append("</form>");
                    }

                         htmlBuilder.append("</td>")
                            .append("<td>")
                            .append("<form style=\"width:83;\" action=\"rateMovie\" method=\"POST\">")
                            .append("<input type=\"hidden\" name=\"id\" value=\"").append(movie.getId()).append("\">")
                            .append("<select name=\"rating\">");
                         if (rated) {
                             htmlBuilder.append("<option value=\"-\">-</option>");
                         } else {
                             htmlBuilder.append("<option value=\"-\" selected>-</option>");
                         }
                    for (int i = 1; i <= 10; i++) {
                        if (i == ratedMovie.map(watchlist -> watchlist.getRating()).orElse(BigDecimal.valueOf(-1)).intValue()) {
                            htmlBuilder.append("<option value=\"").append(i).append("\" selected>").append(i).append("</option>");
                        } else {
                            htmlBuilder.append("<option value=\"").append(i).append("\">").append(i).append("</option>");
                        }
                    }
                    htmlBuilder.append("</select>")
                            .append("<button type=\"submit\">Rate</button>")
                            .append("</form>")
                            .append("</td>");
                }
                htmlBuilder.append("</tr>");
            }
            htmlBuilder.append("</table>");
            entityManager.clear();
        } catch (Exception e) {
            System.out.println("Failed !!! " + e.getMessage());
        }

        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

    @GET
    @Path("/watchlist")
    @Produces({MediaType.TEXT_HTML})
    public String getWatchlist() {
        Integer userId = (Integer) request.getSession().getAttribute("user_id");

        TypedQuery<Watchlist> watchlistQuery = entityManager.createNamedQuery("getAllWatchlist", Watchlist.class);
        watchlistQuery.setParameter("userId", userId);
        List<Watchlist> watchlistItems = watchlistQuery.getResultList();

        String name = (String) request.getSession().getAttribute("username");
        htmlBuilder.append("<p1>Hello " + name + " </p1>");
        htmlBuilder.append("<a href='movies'><button style='background-color: #013220; color: white;'>Movies</button></a>");
        htmlBuilder.append("<a href='rating'><button style='background-color: #013220; color: white;'>Rating</button></a>");
        htmlBuilder.append("<a href='logout'><button style='background-color: #013220; color: white;'>Logout</button></a>");
        htmlBuilder.append("<h1>Watchlist</h1>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>Title</th><th>Director</th><th>Year</th><th>Description</th><th>Watchlist</th></tr>");
        for (Watchlist watchlistItem : watchlistItems) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getTitle()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getAuthor()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getYear()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getDescription()).append("</td>");
            htmlBuilder.append("<td><form action=\"deleteMovieFromWatchlist\" method=\"POST\">")
                    .append("<input type=\"hidden\" name=\"id\" value=\"").append(watchlistItem.getMovie().getId()).append("\">")
                    .append("<input type=\"hidden\" name=\"path\" value=\"/watchlist\">")
                    .append("<button type=\"submit\" style=\"background-color: red;\">Remove</button>")
                    .append("</form>")
                    .append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    @GET
    @Path("/rating")
    @Produces({MediaType.TEXT_HTML})
    public String getRating() {
        Integer userId = (Integer) request.getSession().getAttribute("user_id");

        TypedQuery<Watchlist> watchlistQuery = entityManager.createNamedQuery("getAllRating", Watchlist.class);
        watchlistQuery.setParameter("userId", userId);
        List<Watchlist> watchlistItems = watchlistQuery.getResultList();

        String name = (String) request.getSession().getAttribute("username");
        htmlBuilder.append("<p1>Hello " + name + " </p1>");
        htmlBuilder.append("<a href='movies'><button style='background-color: #013220; color: white;'>Movies</button></a>");
        htmlBuilder.append("<a href='watchlist'><button style='background-color: #013220; color: white;'>Watchlist</button></a>");
        htmlBuilder.append("<a href='logout'><button style='background-color: #013220; color: white;'>Logout</button></a>");
        htmlBuilder.append("<h1>Ratings</h1>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>Title</th><th>Director</th><th>Year</th><th>Description</th><th>Rating</th></tr>");

        for (Watchlist watchlistItem : watchlistItems) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getTitle()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getAuthor()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getYear()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getDescription()).append("</td>")
            .append("<td>")
            .append("<form style=\"width:83;\" action=\"rateMovie\" method=\"POST\">")
            .append("<input type=\"hidden\" name=\"id\" value=\"").append(watchlistItem.getMovie().getId()).append("\">")
            .append("<select name=\"rating\">");
            if (watchlistItem.getRating().intValue() == 0) {
                htmlBuilder.append("<option value=\"-\">-</option>");
            } else {
                htmlBuilder.append("<option value=\"-\" selected>-</option>");
            }
            for (int i = 1; i <= 10; i++) {
                if (i == watchlistItem.getRating().intValue()) {
                    htmlBuilder.append("<option value=\"").append(i).append("\" selected>").append(i).append("</option>");
                } else {
                    htmlBuilder.append("<option value=\"").append(i).append("\">").append(i).append("</option>");
                }
            }
            htmlBuilder.append("</select>")
            .append("<button type=\"submit\">Rate</button>")
            .append("</form>")
            .append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body></html>");
        request.getSession().setAttribute("rate_path", "/rating");
        return htmlBuilder.toString();
    }

    @POST
    @Path("/rateMovie")
    @Produces({MediaType.TEXT_HTML})
    public Response rateMovie(@FormParam("id") Integer movieId, @FormParam("rating") String rating) {
        if (rating.equals("-")) {
            return Response.seeOther(URI.create("/jpa/movies")).build();
        }

        System.out.println("Rating movie");
        System.out.println(movieId);
        System.out.println(rating);
        Integer userId = (Integer) request.getSession().getAttribute("user_id");
        Movie movie = entityManager.find(Movie.class, movieId);
        Users user = entityManager.find(Users.class, userId);


        TypedQuery<Watchlist> watchlistQuery = entityManager.createNamedQuery("selectByUserAndMovie", Watchlist.class);
        watchlistQuery.setParameter("user", user);
        watchlistQuery.setParameter("movie", movie);
        List<Watchlist> watchlistItems = watchlistQuery.getResultList();
        Watchlist watchlistItem;
        if(watchlistItems.isEmpty()) {
            watchlistItem = new Watchlist();
            watchlistItem.setMovie(movie);
            watchlistItem.setUser(user);
            watchlistItem.setRating(new BigDecimal(rating));
            entityTransaction.begin();
            entityManager.persist(watchlistItem);
            entityManager.flush();
            entityTransaction.commit();
        } else {
            watchlistItem = watchlistItems.get(0);
            watchlistItem.setRating(new BigDecimal(rating));
            entityTransaction.begin();
            entityManager.merge(watchlistItem);
            entityTransaction.commit();
        }

        entityTransaction.begin();
        String sql = "SELECT update_movie_average_rating(?)";
        entityManager.createNativeQuery(sql).setParameter(1, movieId).getResultList();
        entityTransaction.commit();
        entityManager.close();
        if (request.getSession().getAttribute("rate_path") != null) {
            request.getSession().setAttribute("rate_path", null);
            return Response.seeOther(URI.create("/jpa/rating")).build();
        }


        return Response.seeOther(URI.create("/jpa/movies")).build();
    }

    @POST
    @Path("/addMovieToWatchlist")
    @Produces(MediaType.TEXT_HTML)
    public Response addMovieToWatchlist(@FormParam("id") Integer movieId) {
        Integer userId = (Integer) request.getSession().getAttribute("user_id");
        Movie movie = entityManager.find(Movie.class, movieId);
        Users user = entityManager.find(Users.class, userId);

        Watchlist newWatchlistItem = new Watchlist();
        newWatchlistItem.setUser(user);
        newWatchlistItem.setMovie(movie);
        entityTransaction.begin();
        entityManager.persist(newWatchlistItem);
        entityManager.flush();
        entityTransaction.commit();

        return Response.seeOther(URI.create("/jpa/movies")).build();
    }

    @POST
    @Path("/deleteMovieFromWatchlist")
    @Produces(MediaType.TEXT_HTML)
    public Response deleteMovieFromWatchlist(@FormParam("id") Integer movieId, @FormParam("path") String path) {
        Integer userId = (Integer) request.getSession().getAttribute("user_id");
        Movie movie = entityManager.find(Movie.class, movieId);
        Users user = entityManager.find(Users.class, userId);

        entityTransaction.begin();
        entityManager.createNamedQuery("Watchlist.deleteByUserAndMovie")
                .setParameter("user", user)
                .setParameter("movie", movie)
                .executeUpdate();

        entityTransaction.commit();

        return Response.seeOther(URI.create("/jpa" + path)).build();
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_HTML)
    public Response logout() {
        request.getSession().setAttribute("username", null);
        request.getSession().setAttribute("user_id", null);
        return Response.seeOther(URI.create("/jpa/movies")).build();
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String getLoginPage() {
        htmlBuilder.append("<form action='login' method='POST'>");
        htmlBuilder.append("<label for='username'>Username:</label></br>");
        htmlBuilder.append("<input type='text' id='username' name='username'></br>");
        htmlBuilder.append("<label for='password'>Password:</label></br>");
        htmlBuilder.append("<input type='password' id='password' name='password'></br>");
        htmlBuilder.append("<input type='submit' value='Login'>");
        htmlBuilder.append("</form>");
        htmlBuilder.append("</br><a href='register'><button style='background-color: #013220; color: white;'>Register</button></a>");
        htmlBuilder.append("<a href='movies'><button style='background-color: #013220; color: white;'>Movies</button></a>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {

        TypedQuery<Users> query = entityManager.createQuery(
                "SELECT u FROM Users u WHERE u.username = :username", Users.class);
        query.setParameter("username", username);

        Users user;
        try {
            user = query.getSingleResult();
            if (password.equals(user.getPassword())) {
                request.getSession().setAttribute("username", username);
                request.getSession().setAttribute("user_id", user.getId());
                return Response.seeOther(URI.create("/jpa/movies")).build();
            } else {
                return Response.seeOther(URI.create("/jpa/login")).build();
            }
        } catch (NoResultException e) {
            return Response.seeOther(URI.create("/jpa/login")).build();
        }
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public String getRegisterPage() {
        htmlBuilder.append("<form action='register' method='POST'>");
        htmlBuilder.append("<label for='username'>Username:</label></br>");
        htmlBuilder.append("<input type='text' id='username' name='username'></br>");
        htmlBuilder.append("<label for='password'>Password:</label></br>");
        htmlBuilder.append("<input type='password' id='password' name='password'></br>");
        htmlBuilder.append("<input type='submit' value='Register'>");
        String error = (String) request.getSession().getAttribute("er_register");
        if (error != null) {
            htmlBuilder.append("<p>This username is already taken</p></br>");
        }

        htmlBuilder.append("</form>");
        htmlBuilder.append("</br><a href='login'><button style='background-color: #013220; color: white;'>Login instead</button></a></br>");
        htmlBuilder.append("<a href='movies'><button style='background-color: #013220; color: white;'>Movies</button></a>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response register(@FormParam("username") String username, @FormParam("password") String password) {
        TypedQuery<Users> query = entityManager.createQuery(
                "SELECT u.username FROM Users u WHERE u.username = :username", Users.class);
        query.setParameter("username", username);

        try {
            query.getSingleResult();

            HttpSession session = request.getSession();
            session.setAttribute("er_register", username);
            return Response.seeOther(URI.create("/jpa/register")).build();
        } catch (NoResultException e) {
            Users user = new Users();
            user.setUsername(username);
            user.setPassword(password);
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.flush();
            entityTransaction.commit();

            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            request.getSession().setAttribute("user_id", user.getId());
            return Response.seeOther(URI.create("/jpa/movies")).build();
        }
    }
}