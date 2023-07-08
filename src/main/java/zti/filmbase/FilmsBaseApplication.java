package zti.filmbase;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the application configuration for the FilmBase API.
 */
@ApplicationPath("/api")
public class FilmsBaseApplication extends Application {
    /**
     * Retrieves the set of resource classes to be included in the application.
     *
     * @return A set of resource classes.
     */
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<Class<?>>();
        set.add(JPAResource.class);
        set.add(FilmBaseWebsocketEndpoint.class);
        return set;
    }
}