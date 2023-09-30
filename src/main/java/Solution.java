import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Solution {
    public static void main(String[] args) {

    }

    public static void findEmployeesWithConsecutiveDays(ExcelObject[] objects) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        // Create a map to store consecutive workdays for each employee
        Map<String, Integer> consecutiveWorkdaysMap = new HashMap<>();
        String currentEmployee = null;
        Set<String> uniqueEmployees = new HashSet<>();
        for (ExcelObject obj : objects) {
            // Check if the employee has changed
            if (obj.employeeName == null) {
                // Skip entries with no employee name
                continue;
            }

            if (!obj.employeeName.equals(currentEmployee)) {
                currentEmployee = obj.employeeName;
                consecutiveWorkdaysMap.put(currentEmployee, 0);
            }

            // Check if both start and end dates are present and not empty
            if (obj.payCycleStartDate != null && obj.payCycleEndDate != null && !obj.payCycleStartDate.isEmpty() && !obj.payCycleEndDate.isEmpty()) {
                try {
                    Date startDate = dateFormat.parse(obj.payCycleStartDate);
                    Date endDate = dateFormat.parse(obj.payCycleEndDate);

                    long difference = endDate.getTime() - startDate.getTime();
                    long daysWorked = difference / (24 * 60 * 60 * 1000);

                    // If the employee worked on consecutive days, increment the count
                    if (daysWorked >= 1) {
                        int consecutiveWorkdays = consecutiveWorkdaysMap.getOrDefault(currentEmployee, 0);
                        consecutiveWorkdays++;
                        consecutiveWorkdaysMap.put(currentEmployee, consecutiveWorkdays);
                    } else {
                        consecutiveWorkdaysMap.put(currentEmployee, 0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // Print employees who have worked more than seven consecutive days
        for (Map.Entry<String, Integer> entry : consecutiveWorkdaysMap.entrySet()) {
            if (entry.getValue() > 7) {
                System.out.println("Employee Name: " + entry.getKey());
            }
        }

        // Print unique employees who meet the criteria
        for (String employeeName : uniqueEmployees) {
            System.out.println("Employee Name: " + employeeName);
        }

    }
    public static void findEmployeesWithTimeBetweenShifts(ExcelObject[] objects) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        // Create a map to store shift end times for each employee
        Map<String, Date> shiftEndTimes = new HashMap<>();
        String currentEmployee = null;
        Set<String> uniqueEmployees = new HashSet<>();
        for (ExcelObject obj : objects) {
            // Check if the time and timeOut fields are not null or empty
            if (obj.employeeName != null && !obj.employeeName.isEmpty() &&
                    obj.time != null && !obj.time.isEmpty() &&
                    obj.timeOut != null && !obj.timeOut.isEmpty()) {
                try {
                    Date currentTime = dateFormat.parse(obj.time);

                    // Check if the employee has changed
                    if (!obj.employeeName.equals(currentEmployee)) {
                        currentEmployee = obj.employeeName;
                        shiftEndTimes.put(currentEmployee, null);
                        continue;
                    }

                    // Check if the employee has a previous shift end time
                    Date previousEndTime = shiftEndTimes.get(currentEmployee);
                    if (previousEndTime != null) {
                        long timeBetweenShifts = currentTime.getTime() - previousEndTime.getTime();
                        long hoursBetweenShifts = timeBetweenShifts / (60 * 60 * 1000); // Convert milliseconds to hours

                        // If the employee has less than 10 hours but more than 1 hour between shifts, print their name
                        if (hoursBetweenShifts < 10 && hoursBetweenShifts > 1) {
                            uniqueEmployees.add("Employee Name: " + currentEmployee);
                        }
                    }

                    // Update the shift end time for the current employee
                    Date endTime = dateFormat.parse(obj.timeOut);
                    shiftEndTimes.put(currentEmployee, endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        // Print unique employees who meet the criteria
        for (String employeeName : uniqueEmployees) {
            System.out.println("Employee Name: " + employeeName);
        }
    }

    public static void findEmployeesWithExcessiveWorkHours(ExcelObject[] objects) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Set<String> uniqueEmployees = new HashSet<>();

        for (ExcelObject obj : objects) {
            try {
                // Check if the time and timeOut fields are not null or empty
                if (obj.employeeName != null && !obj.employeeName.isEmpty() &&
                        obj.time != null && !obj.time.isEmpty() &&
                        obj.timeOut != null && !obj.timeOut.isEmpty()) {

                    Date startTime = dateFormat.parse(obj.time);
                    Date endTime = dateFormat.parse(obj.timeOut);

                    // Calculate the work duration in milliseconds
                    long workDuration = endTime.getTime() - startTime.getTime();

                    // Calculate work duration in hours
                    double hoursWorked = workDuration / (60.0 * 60.0 * 1000.0);

                    // Check if the employee has worked more than 14 hours in a single shift
                    if (hoursWorked > 14.0) {
                        uniqueEmployees.add("Employee Name: " + obj.employeeName + ", Hours Worked: " + hoursWorked);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Print unique employees who meet the criteria
        for (String employeeName : uniqueEmployees) {
            System.out.println(employeeName);
        }

    }
}
