package zti.filmbase;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

@Classes(cdi = true, value = { JPAResource.class })
@Descriptors(@Descriptor(name = "persistence.xml", path = "META-INF/persistence.xml"))
@ContainerProperties({
        @ContainerProperties.Property(name = "myDb", value = "new://Resource?type=DataSource"),
        @ContainerProperties.Property(name = "myDb.JdbcDriver", value = "org.postgresql.Driver"),
        @ContainerProperties.Property(name = "myDb.JdbcUrl", value = "jdbc:postgresql://peanut.db.elephantsql.com:5432/nhpbzgtj"),
        @ContainerProperties.Property(name = "myDb.UserName", value = "nhpbzgtj"),
        @ContainerProperties.Property(name = "myDb.Password", value = "PQdkCLcK3KhC8_Xj7JyY6Trw3uVi_pP-")
})
@RunWith(ApplicationComposer.class)
class FilmsBaseApplicationTest {
    private static JPAResource jpaResource;

    @BeforeAll
    public static void setup() {
        jpaResource = new JPAResource();
    }
    @Test
    public void testLogin_SuccessfulLogin() {
        String username = "admin";
        String password = "admin";
        JsonObject loginData = Json.createObjectBuilder()
                .add("username", username)
                .add("password", password)
                .build();

        String result = jpaResource.login(loginData);
        assertTrue(result.contains("<p1>Hello " + username + "</p1>"));
    }
}