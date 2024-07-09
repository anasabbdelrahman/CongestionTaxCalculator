package congestion.calculator;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// import org.bson.Document;
import org.springframework.stereotype.Service;

// import com.mongodb.client.MongoClients;
// import com.mongodb.client.MongoCollection;
// import com.mongodb.client.MongoDatabase;

@Service
public class CongestionTaxCalculator {
    // private MongoDatabase database;
    private static final int MAX_DAILY_FEE = 60;
    private static final int WINDOW_TIME = 60;
    private static Map<String, Integer> tollFreeVehicles = new HashMap<>();

    // public CongestionTaxCalculator() {
    //     var mongoClient = MongoClients.create("mongodb+srv://anassanwer:<password>@clustertest.dipvwkx.mongodb.net/?retryWrites=true&w=majority&appName=ClusterTest");
    //     this.database = mongoClient.getDatabase("congestionTax");
    // }

    static {
        tollFreeVehicles.put("Motorbike", 0);
        tollFreeVehicles.put("Tractor", 1);
        tollFreeVehicles.put("Emergency", 2);
        tollFreeVehicles.put("Diplomat", 3);
        tollFreeVehicles.put("Foreign", 4);
        tollFreeVehicles.put("Military", 5);
    }

    public int getTax(Vehicle vehicle, Date[] dates) {
        if (dates.length == 0 || isTollFreeVehicle(vehicle)) return 0;
        
        Map<String, Integer> dailyFees = new HashMap<>();
        Date intervalStart = dates[0];
        int currentMaxFee = 0;
        String currentDayKey = getDateKey(intervalStart);

        for (Date date : dates) {
            if (isTollFreeDate(date)) continue;
            String dateKey = getDateKey(date);
            dailyFees.putIfAbsent(dateKey, 0);

            int nextFee = getTollFee(date);
            long diffInMillies = date.getTime() - intervalStart.getTime();
            long minutes = diffInMillies / 1000 / 60;

            if (minutes <= WINDOW_TIME && dateKey.equals(currentDayKey)) {
                // Within the same 60-minute window and same day, take the maximum fee
                currentMaxFee = Math.max(currentMaxFee, nextFee);
            } else {
                // Add the current max fee to the day's total fee
                dailyFees.put(currentDayKey, Math.min(MAX_DAILY_FEE, dailyFees.get(currentDayKey) + currentMaxFee));
                // Reset interval and max fee for the new window
                intervalStart = date;
                currentMaxFee = nextFee;
                currentDayKey = dateKey;
            }
        }
        // Add the last interval's max fee
        dailyFees.put(currentDayKey, Math.min(MAX_DAILY_FEE, dailyFees.get(currentDayKey) + currentMaxFee));

        // Sum up the total fee across all days
        int totalFee = dailyFees.values().stream().mapToInt(Integer::intValue).sum();
        return totalFee;
    }

    private String getDateKey(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // January is 0, need to add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + month + "-" + day;
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        String vehicleType = vehicle.getVehicleType();
        return tollFreeVehicles.containsKey(vehicleType);
    }

    public int getTollFee(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // int taxtfromDB = getTollFeeFromDB(date, vehicle);
        // System.out.println("Tax from the Mongo DB = " + taxtfromDB);
        if (hour == 6 && minute <= 29) return 8;
        if (hour == 6 && minute >= 30) return 13;
        if (hour == 7) return 18;
        if (hour == 8 && minute <= 29) return 13;
        if (hour >= 8 && hour <= 14) return 8;
        if (hour == 15 && minute <= 29) return 13;
        if ((hour == 15 && minute >= 30) || (hour == 16)) return 18;
        if (hour == 17) return 13;
        if (hour == 18 && minute <= 29) return 8;
        return 0;
    }

    //  public int getTollFeeFromDB(Date date, Vehicle vehicle) {
    //     if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) return 0;

    //     Calendar calendar = Calendar.getInstance();
    //     calendar.setTime(date);
    //     int hour = calendar.get(Calendar.HOUR_OF_DAY);
    //     int minute = calendar.get(Calendar.MINUTE);

    //     MongoCollection<Document> collection = database.getCollection("taxRates");
    //     Document query = new Document("startHour", new Document("$lte", hour))
    //             .append("endHour", new Document("$gte", hour))
    //             .append("startMinute", new Document("$lte", minute))
    //             .append("endMinute", new Document("$gte", minute));

    //     Document taxRate = collection.find(query).first();
    //     return taxRate != null ? taxRate.getInteger("amount") : 0;
    // }

    private boolean isTollFreeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) return true;

        if (year == 2013) {
            if ((month == 1 && dayOfMonth == 1) ||
                (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                (month == 7) ||
                (month == 11 && dayOfMonth == 1) ||
                (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31))) {
                return true;
            }
        }
        return false;
    }
}