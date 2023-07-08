package zti.filmbase;

import jakarta.json.JsonObject;
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

@Path("/jpa")
public class JPAResource {
    private EntityManagerFactory managerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    @Context
    private HttpServletRequest request;
    private StringBuilder htmlBuilder;
    public JPAResource() {
        managerFactory = createEntityManagerFactory("PU_Postgresql");
        entityManager = managerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     *
     * @return
     */
    @GET
    @Path("/movies")
    @Produces({MediaType.TEXT_HTML})
    public String getMovies() {
        htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<title>ZTI - film</title>");
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
        htmlBuilder.append("<script>");
        htmlBuilder.append("function getMovies() {")
        .append("fetch('http://localhost:8080/film-base-1.0-SNAPSHOT/api/jpa/movies')")
            .append(".then(response => response.text())")
            .append(".then(data => {")
            .append("const parser = new DOMParser();")
            .append("const newDocument = parser.parseFromString(data, 'text/html');")
            .append("document.head.innerHTML = newDocument.head.innerHTML;")
            .append("document.body.innerHTML = newDocument.body.innerHTML;")
            .append("})")
            .append(".catch(error => {")
            .append("console.error('Error:', error);")
            .append("});")
        .append("}");
        htmlBuilder.append("var socket = new WebSocket(\"ws://localhost:8080/film-base-1.0-SNAPSHOT/websocket\");\n")
            .append("socket.onopen = function() {\n")
                .append("console.log(\"WebSocket connection opened\");\n")
            .append("};\n")
            .append("socket.onmessage = function(event) {\n")
                .append("var data = event.data;\n")
                .append("document.body.innerHTML = data;\n")
            .append("};\n")
            .append("socket.onerror = function(error) {\n")
                .append("console.error(\"WebSocket connection error:\", error);\n")
            .append("};\n")
            .append("socket.onclose = function() {\n")
                .append("console.log(\"WebSocket connection closed\");\n")
            .append("};\n");
        htmlBuilder.append("function getLogin() {\n")
            .append("socket.send(\"getLogin\");\n")
        .append("}\n");
        htmlBuilder.append("function getRegister() {")
        .append("fetch('http://localhost:8080/film-base-1.0-SNAPSHOT/api/jpa/register')")
        .append(".then(response => response.text())")
        .append(".then(data => {")
        .append("document.body.innerHTML = data;")
        .append("})")
        .append(".catch(error => {")
        .append("console.error('Error:', error);")
        .append("});")
        .append("}");
        htmlBuilder.append("function postLogin() {");
        htmlBuilder.append("  var form = document.getElementById('loginForm');");
        htmlBuilder.append("  var formData = new FormData(form);");
        htmlBuilder.append("  var jsonObject = {};");
        htmlBuilder.append("  for (var [key, value] of formData.entries()) {");
        htmlBuilder.append("    jsonObject[key] = value;");
        htmlBuilder.append("  }");
        htmlBuilder.append("  fetch('login', {");
        htmlBuilder.append("    method: 'POST',");
        htmlBuilder.append("    headers: {");
        htmlBuilder.append("      'Content-Type': 'application/json'");
        htmlBuilder.append("    },");
        htmlBuilder.append("    body: JSON.stringify(jsonObject)");
        htmlBuilder.append("  })");
        htmlBuilder.append("    .then(response => response.text())");
        htmlBuilder.append("    .then(data => {");
        htmlBuilder.append("document.body.innerHTML = data;");
        htmlBuilder.append("    })");
        htmlBuilder.append("    .catch(error => {");
        htmlBuilder.append("      console.error(error);");
        htmlBuilder.append("    });");
        htmlBuilder.append("}");
        htmlBuilder.append("function postRegister() {");
        htmlBuilder.append("  var form = document.getElementById('registerForm');");
        htmlBuilder.append("  var formData = new FormData(form);");
        htmlBuilder.append("  var jsonObject = {};");
        htmlBuilder.append("  for (var [key, value] of formData.entries()) {");
        htmlBuilder.append("    jsonObject[key] = value;");
        htmlBuilder.append("  }");
        htmlBuilder.append("  fetch('register', {");
        htmlBuilder.append("    method: 'POST',");
        htmlBuilder.append("    headers: {");
        htmlBuilder.append("      'Content-Type': 'application/json'");
        htmlBuilder.append("    },");
        htmlBuilder.append("    body: JSON.stringify(jsonObject)");
        htmlBuilder.append("  })");
        htmlBuilder.append("    .then(response => response.text())");
        htmlBuilder.append("    .then(data => {");
        htmlBuilder.append("document.body.innerHTML = data;");
        htmlBuilder.append("    })");
        htmlBuilder.append("    .catch(error => {");
        htmlBuilder.append("      console.error(error);");
        htmlBuilder.append("    });");
        htmlBuilder.append("}");
        htmlBuilder.append("function getLogout() {")
                .append("fetch('logout')")
                .append(".then(response => response.text())")
                .append(".then(data => {")
                .append("document.body.innerHTML = data;")
                .append("})")
                .append(".catch(error => {")
                .append("console.error('Error:', error);")
                .append("});")
                .append("}");
        htmlBuilder.append("function getWatchlist() {")
                .append("fetch('watchlist')")
                .append(".then(response => response.text())")
                .append(".then(data => {")
                .append("document.body.innerHTML = data;")
                .append("})")
                .append(".catch(error => {")
                .append("console.error('Error:', error);")
                .append("});")
                .append("}");
        htmlBuilder.append("function deleteMovieFromWatchlist(id, redirectTo) {")
            .append("  fetch('deleteMovieFromWatchlist', {")
            .append("    method: 'DELETE',")
            .append("    headers: {")
            .append("      'Content-Type': 'application/json'")
            .append("    },")
            .append("    body: JSON.stringify({ id: id, site: redirectTo })")
            .append("  })")
            .append("    .then(response => response.text())")
            .append("    .then(data => {")
            .append("document.body.innerHTML = data;")
            .append("      ")
            .append("    })")
            .append("    .catch(error => {")
            .append("      console.error('Error:', error);")
            .append("    });")
        .append("}");
        htmlBuilder.append("function addMovieToWatchlist(id) {")
                .append("  fetch('addMovieToWatchlist', {")
                .append("    method: 'POST',")
                .append("    headers: {")
                .append("      'Content-Type': 'application/json'")
                .append("    },")
                .append("    body: JSON.stringify({ id: id})")
                .append("  })")
                .append("    .then(response => response.text())")
                .append("    .then(data => {")
                .append("document.body.innerHTML = data;")
                .append("      ")
                .append("    })")
                .append("    .catch(error => {")
                .append("      console.error('Error:', error);")
                .append("    });")
                .append("}");
        htmlBuilder.append("function getRating() {")
            .append("fetch('rating')")
            .append(".then(response => response.text())")
            .append(".then(data => {")
            .append("document.body.innerHTML = data;")
            .append("})")
            .append(".catch(error => {")
            .append("console.error('Error:', error);")
            .append("});")
        .append("}");
        htmlBuilder.append("function rateMovie(form) {")
            .append("  var formData = new FormData(form);")
            .append("  var id = parseInt(formData.get('id'), 10);")
            .append("  var rating = formData.get('rating');")
            .append("  fetch('rateMovie', {")
            .append("    method: 'POST',")
            .append("    headers: {")
            .append("      'Content-Type': 'application/json'")
            .append("    },")
            .append("    body: JSON.stringify({ id: id, rating: rating})")
            .append("  })")
            .append("    .then(response => response.text())")
            .append("    .then(data => {")
            .append("document.body.innerHTML = data;")
            .append("    })")
            .append("    .catch(error => {")
            .append("      console.error('Error:', error);")
            .append("    });")
        .append("}");
        htmlBuilder.append("</script>");
        htmlBuilder.append("</head>");
        getMoviesBody();
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

    public void getMoviesBody() {
        htmlBuilder.append("<body>");
        htmlBuilder.append("<div style='text-align: center;'>");
        String name = (String) request.getSession().getAttribute("username");
        if (name != null) {
            htmlBuilder.append("<p1>Hello " + name + " </p1>");
            htmlBuilder.append("<button onclick='getWatchlist()' style='background-color: #013220; color: white;'>Watchlist</button>");
            htmlBuilder.append("<button onclick='getRating()' style='background-color: #013220; color: white;'>Rating</button>");
            htmlBuilder.append("<button onclick='getLogout()' style='background-color: #013220; color: white;'>Logout</button>");
        } else {
            htmlBuilder.append("<button onclick=\"getLogin()\" style='background-color: #013220; color: white;'>Login</button>");
            htmlBuilder.append("<button onclick=\"getRegister()\" style='background-color: #013220; color: white;'>Register</button>");
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
                        htmlBuilder.append("<form>")
                        .append("<button type=\"button\" onclick=\"deleteMovieFromWatchlist(").append(movie.getId()).append(", 'movies')\" style=\"background-color: red;\">Remove</button>")
                        .append("</form>");
                    } else if (!ratedMovie.isEmpty()) {
                        rated = true;
                        htmlBuilder .append("<button type=\"button\" style=\"background-color: gray; width:91;\" disabled>Movie rated</button>");
                    } else {
                        htmlBuilder.append("<form>")
                            .append("<button type=\"button\" onclick=\"addMovieToWatchlist(").append(movie.getId()).append(")\" style=\"background-color: green;\">Add</button>")
                            .append("</form>");
                    }

                    htmlBuilder.append("</td>")
                            .append("<td>")
                            .append("<form style=\"width:83;\">")
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
                            .append("<button type=\"button\" onclick=\"rateMovie(this.form)\">Rate</button>")
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
    }

    @GET
    @Path("/watchlist")
    @Produces({MediaType.TEXT_HTML})
    public String getWatchlist() {
        htmlBuilder = new StringBuilder();
        htmlBuilder.append("<body>");
        Integer userId = (Integer) request.getSession().getAttribute("user_id");

        TypedQuery<Watchlist> watchlistQuery = entityManager.createNamedQuery("getAllWatchlist", Watchlist.class);
        watchlistQuery.setParameter("userId", userId);
        List<Watchlist> watchlistItems = watchlistQuery.getResultList();

        String name = (String) request.getSession().getAttribute("username");
        htmlBuilder.append("<div style='text-align: center;'>");
        htmlBuilder.append("<p1>Hello " + name + " </p1>");
        htmlBuilder.append("<button onclick='getMovies()' style='background-color: #013220; color: white;'>Movies</button>");
        htmlBuilder.append("<button onclick='getRating()' style='background-color: #013220; color: white;'>Rating</button>");
        htmlBuilder.append("<button onclick='getLogout()' style='background-color: #013220; color: white;'>Logout</button>");
        htmlBuilder.append("<h1>Watchlist</h1>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>Title</th><th>Director</th><th>Year</th><th>Description</th><th>Watchlist</th></tr>");
        for (Watchlist watchlistItem : watchlistItems) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getTitle()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getAuthor()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getYear()).append("</td>");
            htmlBuilder.append("<td>").append(watchlistItem.getMovie().getDescription()).append("</td>");
            htmlBuilder.append("<td><form>")
                    .append("<button type=\"button\" onclick=\"deleteMovieFromWatchlist(").append(watchlistItem.getMovie().getId()).append(", 'watchlist')\" style=\"background-color: red;\">Remove</button>")
                    .append("</form>")
                    .append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");

        return htmlBuilder.toString();
    }

    @GET
    @Path("/rating")
    @Produces({MediaType.TEXT_HTML})
    public String getRating() {
        htmlBuilder = new StringBuilder();
        htmlBuilder.append("<body>");
        Integer userId = (Integer) request.getSession().getAttribute("user_id");

        TypedQuery<Watchlist> watchlistQuery = entityManager.createNamedQuery("getAllRating", Watchlist.class);
        watchlistQuery.setParameter("userId", userId);
        List<Watchlist> watchlistItems = watchlistQuery.getResultList();

        String name = (String) request.getSession().getAttribute("username");
        htmlBuilder.append("<div style='text-align: center;'>");
        htmlBuilder.append("<p1>Hello " + name + " </p1>");
        htmlBuilder.append("<button onclick='getMovies()' style='background-color: #013220; color: white;'>Movies</button>");
        htmlBuilder.append("<button onclick='getWatchlist()' style='background-color: #013220; color: white;'>Watchlist</button>");
        htmlBuilder.append("<button onclick='getLogout()' style='background-color: #013220; color: white;'>Logout</button>");
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
            .append("<form style=\"width:83;\">")
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
            .append("<button type=\"button\" onclick=\"rateMovie(this.form)\">Rate</button>")
            .append("</form>")
            .append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        request.getSession().setAttribute("rate_path", "/rating");
        return htmlBuilder.toString();
    }

    @POST
    @Path("/rateMovie")
    @Produces({MediaType.TEXT_HTML})
    @Consumes({MediaType.APPLICATION_JSON})
    public String rateMovie(JsonObject rateData) {
        Integer movieId = rateData.getInt("id");
        String rating = rateData.getString("rating");
        if (rating.equals("-")) {
            htmlBuilder = new StringBuilder();
            getMoviesBody();
            return htmlBuilder.toString();
        }

        System.out.println("Rating movie");
        System.out.println("Movie ID: " + movieId);
        System.out.println("Rate: " + rating);
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
        if (request.getSession().getAttribute("rate_path") != null) {
            request.getSession().setAttribute("rate_path", null);
            System.out.println("Returning rating page");
            return getRating();
        }


        System.out.println("Returning movies page");
        htmlBuilder = new StringBuilder();
        getMoviesBody();
        System.out.println(htmlBuilder);
        return htmlBuilder.toString();
    }

    @POST
    @Path("/addMovieToWatchlist")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addMovieToWatchlist(JsonObject watchlistData) {
        Integer movieId = watchlistData.getInt("id");
        Integer userId = (Integer) request.getSession().getAttribute("user_id");
        Movie movie = entityManager.find(Movie.class, movieId);
        Users user = entityManager.find(Users.class, userId);

        Query query = entityManager.createNamedQuery("Watchlist.exists");
        query.setParameter("userId", userId);
        query.setParameter("movieId", movieId);
        boolean movieAlreadyAdded = ((Long) query.getSingleResult()) > 0;

        if (!movieAlreadyAdded) {
            Watchlist newWatchlistItem = new Watchlist();
            newWatchlistItem.setUser(user);
            newWatchlistItem.setMovie(movie);
            entityTransaction.begin();
            entityManager.persist(newWatchlistItem);
            entityManager.flush();
            entityTransaction.commit();
        }

        htmlBuilder = new StringBuilder();
        getMoviesBody();
        return htmlBuilder.toString();
    }

    @DELETE
    @Path("/deleteMovieFromWatchlist")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteMovieFromWatchlist(JsonObject watchlistData) {
        Integer movieId = watchlistData.getInt("id");
        Integer userId = (Integer) request.getSession().getAttribute("user_id");
        Movie movie = entityManager.find(Movie.class, movieId);
        Users user = entityManager.find(Users.class, userId);

        entityTransaction.begin();
        entityManager.createNamedQuery("Watchlist.deleteByUserAndMovie")
                .setParameter("user", user)
                .setParameter("movie", movie)
                .executeUpdate();

        entityTransaction.commit();

        if (watchlistData.getString("site").equals("watchlist"))
            return getWatchlist();

        htmlBuilder = new StringBuilder();
        getMoviesBody();
        return htmlBuilder.toString();
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_HTML)
    public String logout() {
        request.getSession().setAttribute("username", null);
        request.getSession().setAttribute("user_id", null);
        htmlBuilder = new StringBuilder();
        getMoviesBody();
        return htmlBuilder.toString();
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String getLoginPage() {
        htmlBuilder = new StringBuilder();
        htmlBuilder.append("<body>");
        htmlBuilder.append("<div style='text-align: center;'>");
        htmlBuilder.append("<form id='loginForm'>");
        htmlBuilder.append("<label for='username'>Username:</label><br/>");
        htmlBuilder.append("<input type='text' id='username' name='username'><br/>");
        htmlBuilder.append("<label for='password'>Password:</label><br/>");
        htmlBuilder.append("<input type='password' id='password' name='password'><br/>");
        htmlBuilder.append("<input type='button' value='Login' onclick='postLogin()'>");
        htmlBuilder.append("</form>");
        htmlBuilder.append("</br><button onclick=\"getRegister()\" style='background-color: #013220; color: white;'>Register</button>");
        htmlBuilder.append("<button onclick=\"getMovies()\" style='background-color: #013220; color: white;'>Movies</button>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");

        return htmlBuilder.toString();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public String login(JsonObject loginData) {
        String username = loginData.getString("username");
        String password = loginData.getString("password");

        System.out.println("Trying to login as:");
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        TypedQuery<Users> query = entityManager.createQuery(
                "SELECT u FROM Users u WHERE u.username = :username", Users.class);
        query.setParameter("username", username);

        System.out.println("Searching user " + username + " in the database");
        Users user;
        try {
            user = query.getSingleResult();
            if (password.equals(user.getPassword())) {
                request.getSession().setAttribute("username", username);
                request.getSession().setAttribute("user_id", user.getId());
                System.out.println("Successfully logged in");
                htmlBuilder = new StringBuilder();
                getMoviesBody();
                return htmlBuilder.toString();
            } else {
                System.out.println("Wrong password");
                return getLoginPage();
            }
        } catch (NoResultException e) {
            System.out.println("No result found in the database: " + e.getMessage());
            return getLoginPage();
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
            return getLoginPage();
        }
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public String getRegisterPage() {
        htmlBuilder = new StringBuilder();
        htmlBuilder.append("<body>");
        htmlBuilder.append("<div style='text-align: center;'>");
        htmlBuilder.append("<form id='registerForm'>");
        htmlBuilder.append("<label for='username'>Username:</label></br>");
        htmlBuilder.append("<input type='text' id='username' name='username'></br>");
        htmlBuilder.append("<label for='password'>Password:</label></br>");
        htmlBuilder.append("<input type='password' id='password' name='password'></br>");
        htmlBuilder.append("<input type='button' value='Register' onclick='postRegister()'>");
        String error = (String) request.getSession().getAttribute("er_register");
        if (error != null) {
            htmlBuilder.append("<p>This username is already taken</p></br>");
        }

        htmlBuilder.append("</form>");
        htmlBuilder.append("</br><button onclick=\"getLogin()\" style='background-color: #013220; color: white;'>Login instead</button>");
        htmlBuilder.append("<button onclick=\"getMovies()\" style='background-color: #013220; color: white;'>Movies</button>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");

        return htmlBuilder.toString();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public String register(JsonObject loginData) {
        String username = loginData.getString("username");
        String password = loginData.getString("password");

        System.out.println("Trying to register as:");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        TypedQuery<Users> query = entityManager.createQuery(
                "SELECT u.username FROM Users u WHERE u.username = :username", Users.class);
        query.setParameter("username", username);

        try {
            query.getSingleResult();

            HttpSession session = request.getSession();
            session.setAttribute("er_register", username);

            System.out.println("Registration failed username " + username + "already taken");
            htmlBuilder = new StringBuilder();
            getRegisterPage();
            return htmlBuilder.toString();
//            return Response.seeOther(URI.create("/jpa/register")).build();
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

            System.out.println("Successfully registered");
            htmlBuilder = new StringBuilder();
            getMoviesBody();
            return htmlBuilder.toString();
//            return Response.seeOther(URI.create("/jpa/movies")).build();
        }
    }
}