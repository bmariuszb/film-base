package zti.filmbase;

import jakarta.ws.rs.core.Response;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JPAResourceTest {
    private JPAResource resource;

    public JPAResourceTest() {
        this.resource = new JPAResource();
    }

    @Test
    public void testGetMovies() {
        String expectedResponse = "<html>...</html>"; // Replace with the expected HTML response

        String actualResponse = resource.getMovies();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetWatchlist() {
        String expectedResponse = "<html>...</html>"; // Replace with the expected HTML response

        String actualResponse = resource.getWatchlist();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testRateMovie() {
        // Set up any necessary mock objects or dependencies

//        Response response = resource.rateMovie(movieId, rating);
//
//        assertEquals(Response.Status.SEE_OTHER.getStatusCode(), response.getStatus());
//        assertEquals(expectedLocation, response.getLocation().toString());
    }
}