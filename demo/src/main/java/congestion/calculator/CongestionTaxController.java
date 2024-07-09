package congestion.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/congestion-tax")
public class CongestionTaxController {

    @Autowired
    private CongestionTaxCalculator congestionTaxCalculator;

    @GetMapping("/calculate")
    public int calculateTax(@RequestParam String vehicleType, @RequestParam String[] dates) {
        Vehicle vehicle = createVehicle(vehicleType);
        Date[] dateArray = parseDates(dates);
        return congestionTaxCalculator.getTax(vehicle, dateArray);
    }

    private Vehicle createVehicle(String vehicleType) {
        switch (vehicleType.toLowerCase()) {
            case "car":
                return new Car();
            case "motorbike":
                return new Motorbike();
            // Add other vehicle types here as needed
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }

    private Date[] parseDates(String[] dates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date[] dateArray = new Date[dates.length];
        for (int i = 0; i < dates.length; i++) {
            try {
                dateArray[i] = sdf.parse(dates[i]);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + dates[i], e);
            }
        }
        return dateArray;
    }
}
