package congestion.calculator.demo;

import org.junit.jupiter.api.Test;

import congestion.calculator.Car;
import congestion.calculator.CongestionTaxCalculator;
import congestion.calculator.Motorbike;
import congestion.calculator.Vehicle;


import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CongestionTaxCalculatorTest {
    CongestionTaxCalculator calculator = new CongestionTaxCalculator();
    Vehicle car = new Car();
    Vehicle motorbike = new Motorbike();

      Date[] dates = {
                getDate(2013, Calendar.JANUARY, 14, 21, 0),  // 0 SEK
                getDate(2013, Calendar.JANUARY, 15, 21, 0),  // 0 SEK
                getDate(2013, Calendar.FEBRUARY, 7, 6, 23),  // 8 SEK
                getDate(2013, Calendar.FEBRUARY, 7, 15, 27), // 13 SEK
                getDate(2013, Calendar.FEBRUARY, 8, 6, 27),  // 8 SEK  
                getDate(2013, Calendar.FEBRUARY, 8, 6, 20),  // 8 SEK but in the same window
                getDate(2013, Calendar.FEBRUARY, 8, 14, 35), // 8 SEK 
                getDate(2013, Calendar.FEBRUARY, 8, 15, 29), // 13 SEK but in the same window but max
                getDate(2013, Calendar.FEBRUARY, 8, 15, 47), // 18 SEK
                getDate(2013, Calendar.FEBRUARY, 8, 16, 1),  // 18 SEK but in the same window
                getDate(2013, Calendar.FEBRUARY, 8, 16, 48), // 18 SEK 
                getDate(2013, Calendar.FEBRUARY, 8, 17, 49), // 13 SEK but exceed daily max, fees only 3 SEK
                getDate(2013, Calendar.FEBRUARY, 8, 18, 29), // reach the max (60 SEK) for 8th Feb
                getDate(2013, Calendar.FEBRUARY, 8, 18, 35), // reach the max (60 SEK) for 8th Feb
                getDate(2013, Calendar.MARCH, 26, 14, 25),   // 8 SEK
                getDate(2013, Calendar.MARCH, 28, 14, 7)     // Public holiday 
        };

    private Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTime();
    }
    
    @Test
    public void TestCarCongestionTax () {
        int tax = calculator.getTax(car, dates); // Car is not exempt
        assertEquals(89, tax); // Update the value based on the expected result
    }

    @Test
    public void TestMotorbikeCongestionTax () {
        int tax = calculator.getTax(motorbike, dates); // Motorbike is tax exempt 
        assertEquals(0, tax); // Update the value based on the expected result
    }
}