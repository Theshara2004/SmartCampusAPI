package theshara.smartcampusapi;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) {
        final ResourceConfig rc = new ResourceConfig().packages("theshara.smartcampusapi.resources");
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        System.out.println("Smart Campus API Started!");
        System.out.println("Discovery URL: " + BASE_URI);
    }
}