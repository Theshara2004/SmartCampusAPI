package theshara.smartcampusapi.resources;

import theshara.smartcampusapi.data.DataStore;
import theshara.smartcampusapi.models.SensorReading;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;


public class SensorReadingResource {

    private String sensorId;

    
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        List<SensorReading> sensorReadings = DataStore.readings.get(sensorId);
        return Response.ok(sensorReadings).build();
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        
        theshara.smartcampusapi.models.Sensor parentSensor = DataStore.sensors.get(sensorId);
        
        
        if ("MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new theshara.smartcampusapi.exceptions.SensorUnavailableException(
                    "Sensor " + sensorId + " is in MAINTENANCE mode and cannot accept readings.");
        }

        if (reading.getId() == null) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        
        DataStore.readings.get(sensorId).add(reading);
        
        
        parentSensor.setCurrentValue(reading.getValue());
        
        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}