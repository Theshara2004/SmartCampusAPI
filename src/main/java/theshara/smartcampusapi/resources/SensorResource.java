package theshara.smartcampusapi.resources;

import theshara.smartcampusapi.data.DataStore;
import theshara.smartcampusapi.models.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sensors")
public class SensorResource {

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(DataStore.sensors.values());
        
        
        if (type != null && !type.trim().isEmpty()) {
            List<Sensor> filtered = allSensors.stream()
                    .filter(s -> type.equalsIgnoreCase(s.getType()))
                    .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }
        
        
        return Response.ok(allSensors).build();
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Sensor ID is required\"}").build();
        }
        
        
        if (sensor.getRoomId() != null && !DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new theshara.smartcampusapi.exceptions.LinkedResourceNotFoundException(
                    "Cannot create sensor. Room ID " + sensor.getRoomId() + " does not exist.");
        }
        
        DataStore.sensors.put(sensor.getId(), sensor);
        DataStore.readings.put(sensor.getId(), new ArrayList<>());
        
        
        if (sensor.getRoomId() != null) {
            DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        }
        
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        
        if (!DataStore.sensors.containsKey(sensorId)) {
            throw new NotFoundException("Sensor not found");
        }
        return new SensorReadingResource(sensorId);
    }
}