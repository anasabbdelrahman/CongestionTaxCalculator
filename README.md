# Congestion Tax Calculator

## Overview
This project calculates the congestion tax charged during fixed hours for vehicles driving into and out of Gothenburg.

## Features
- Calculate the total congestion tax for a list of dates based on time intervals.
- The maximum daily fee is 60 SEK.
- Returns the total fee for taxed vehicles (e.g., cars).
- Returns zero for tax-exempt vehicles (e.g., motorbikes).

## Prerequisites
Ensure you have met the following requirements:
- **Java Development Kit (JDK) 8 or later**: You need the JDK to compile and run Java applications.

## Installation
Follow these steps to install and set up your project:

```sh
# Clone the repository
git clone https://github.com/anasabbdelrahman/CongestionTaxCalculator.git

# Run the application using JUnit test
./mvnw clean test

# Check CongestionTaxCalculator.java class to find the list of dates and assert the results

# Run the using Spring boot
./mvnw spring-boot:run

# To calculate the congestion tax fee for a vehicle, send a GET request using Postman or browser in following URL format:
http://localhost:8080/api/congestion-tax/calculate?vehicleType={vehicleType}&dates={date1}&dates={date2}&...

# For example, to calculate the total tax fee for a car for a list of dates, use below URL
http://localhost:8080/api/congestion-tax/calculate?vehicleType=car&dates=2013-01-14 21:00:00&dates=2013-01-15 21:00:00&dates=2013-02-07 06:23:27&dates=2013-02-07 15:27:00&dates=2013-02-08 06:27:00&dates=2013-02-08 06:20:00&dates=2013-02-08 14:35:00&dates=2013-02-08 15:29:00&dates=2013-02-08 15:47:00&dates=2013-02-08 16:01:00&dates=2013-02-08 16:48:00&dates=2013-02-08 17:49:00&dates=2013-02-08 18:29:00&dates=2013-02-08 18:35:00&dates=2013-03-26 14:25:00&dates=2013-03-28 14:07:00
```

# Future Improvment
- Replace GET API with POST using JSON object, which can handle list of vehicles.
- Also can handle huge numbe of dates.
- Using document database such MongoDB to handle the intervals and prices more dynamic. Also can add other cities with different tax charges.
