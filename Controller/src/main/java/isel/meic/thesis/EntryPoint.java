package isel.meic.thesis;
import isel.meic.thesis.core.Register;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class EntryPoint {

    protected Register register = new Register();


    @GetMapping("/status")
    public String getStatus() {
        return "ESP32 is online";
    }

    @PostMapping("/register")
    public String registerDevice(@RequestBody String data) {
        //data to JSON
        JSONObject jsonObject = new JSONObject(data);

        //create device
        Device device = new Device(jsonObject.getString("device"), jsonObject.getString("mac"), jsonObject.getInt("type"));

        //add device to register
        register.addDevice(device);

        System.out.println("Device registered: " + device.toString());
        return "Device registered successfully";
    }

   /* @PostMapping("/data")
    public String receiveData(@RequestBody String data) {

        //data to JSON
        JSONObject jsonObject = new JSONObject(data);

        //check if device in map via MAC address
        if(devices.containsKey(jsonObject.getString("mac"))){
            System.out.println("Device already in map");
        } else {
            //add device to map

            Device device = new Device(jsonObject.getString("device"), jsonObject.getString("mac"), jsonObject.getInt("type"));
            devices.put(jsonObject.getString("mac"), device);
            System.out.println("Device added to map");
        }

        System.out.println("Received data from ESP32: " + data);
        return "Data received successfully";
    }*/

    @GetMapping("/getAll")
    public String getAllDevices() {
        return register.getDevices().toString();
    }
}
