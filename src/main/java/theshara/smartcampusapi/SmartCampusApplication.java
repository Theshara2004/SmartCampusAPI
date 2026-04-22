package theshara.smartcampusapi;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/api/v1")
public class SmartCampusApplication extends ResourceConfig {
    
    public SmartCampusApplication() {
        
        packages("theshara.smartcampusapi.resources", "theshara.smartcampusapi.exceptions");
        
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
    }
}