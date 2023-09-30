import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelToObjects {
    public static void main(String[] args) {
        String excelFilePath = "/Users/nishupanwar/Downloads/Assignment_Timecard.xlsx"; // Replace with your file path
        List<ExcelObject> objects = readExcel(excelFilePath);
        if (!objects.isEmpty()) {
            objects.remove(0);
        }
        ExcelObject[] objectsArray = objects.toArray(new ExcelObject[0]);
//        System.out.println(objectsArray[1]);
        System.out.println("Ques-1 - ");
        Solution.findEmployeesWithConsecutiveDays(objectsArray);
        System.out.println("....................................");
        System.out.println("Ques-2 - ");
        Solution.findEmployeesWithTimeBetweenShifts(objectsArray);
        System.out.println("....................................");
        System.out.println("Ques-3 - ");
        Solution.findEmployeesWithExcessiveWorkHours(objectsArray);
        System.out.println("....................................");

    }

    public static List<ExcelObject> readExcel(String excelFilePath) {
        List<ExcelObject> objects = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming you're reading the first sheet

            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();

                // Assuming the columns are in the same order as specified
                String positionId = getCellValueAsString(currentRow.getCell(0));
                String positionStatus = getCellValueAsString(currentRow.getCell(1));
                String time = getCellValueAsString(currentRow.getCell(2));
                String timeOut = getCellValueAsString(currentRow.getCell(3));
                String timecardHours = getCellValueAsString(currentRow.getCell(4));
                String payCycleStartDate = getCellValueAsString(currentRow.getCell(5));
                String payCycleEndDate = getCellValueAsString(currentRow.getCell(6));
                String employeeName = getCellValueAsString(currentRow.getCell(7));
                String fileNumber = getCellValueAsString(currentRow.getCell(8));

                ExcelObject obj = new ExcelObject(positionId, positionStatus, time, timeOut, timecardHours,
                        payCycleStartDate, payCycleEndDate, employeeName, fileNumber);
                objects.add(obj);
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }

        return objects;
    }
    private static String getCellValueAsString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return ""; // Return an empty string if cell is null or blank
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            // Check if it's a valid date
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                return dateFormat.format(date);
            }
        }

        return ""; // Return an empty string if cell value cannot be parsed
    }
}

class ExcelObject {
    public String positionId;
    public String positionStatus;
    public String time;
    public String timeOut;
    public String timecardHours;
    public String payCycleStartDate;
    public String payCycleEndDate;
    public String employeeName;
    public String fileNumber;

    public ExcelObject(String positionId, String positionStatus, String time, String timeOut, String timecardHours,
                       String payCycleStartDate, String payCycleEndDate, String employeeName, String fileNumber) {
        this.positionId = positionId;
        this.positionStatus = positionStatus;
        this.time = time;
        this.timeOut = timeOut;
        this.timecardHours = timecardHours;
        this.payCycleStartDate = payCycleStartDate;
        this.payCycleEndDate = payCycleEndDate;
        this.employeeName = employeeName;
        this.fileNumber = fileNumber;
    }

    // Implement getters and setters as needed...

    @Override
    public String toString() {
        return "ExcelObject{" +
                "positionId=" + positionId +
                ", positionStatus='" + positionStatus + '\'' +
                ", time='" + time + '\'' +
                ", timeOut='" + timeOut + '\'' +
                ", timecardHours=" + timecardHours +
                ", payCycleStartDate=" + payCycleStartDate +
                ", payCycleEndDate=" + payCycleEndDate +
                ", employeeName='" + employeeName + '\'' +
                ", fileNumber='" + fileNumber + '\'' +
                '}';
    }
}
