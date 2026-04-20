package theshara.smartcampusapi.data;

import theshara.smartcampusapi.models.Room;
import theshara.smartcampusapi.models.Sensor;
import theshara.smartcampusapi.models.SensorReading;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class DataStore {
    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();
    
    public static Map<String, List<SensorReading>> readings = new HashMap<>(); 
}