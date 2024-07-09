package congestion.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/congestion-tax")
public class CongestionTaxController {

    @Autowired
    private CongestionTaxCalculator congestionTaxCalculator;
    private static final Logger logger = LoggerFactory.getLogger(CongestionTaxController.class);

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateTax(@RequestParam String vehicleType, @RequestParam String[] dates) {
        try {
            Vehicle vehicle = createVehicle(vehicleType);
            Date[] dateArray = parseDates(dates);
            int tax = congestionTaxCalculator.getTax(vehicle, dateArray);
            return new ResponseEntity<>(tax, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Unknown vehicle type: " + vehicleType, e);
            return new ResponseEntity<>("Unknown vehicle type: " + vehicleType, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error occurred while calculating tax", e);
            return new ResponseEntity<>("An error occurred while calculating the tax", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
