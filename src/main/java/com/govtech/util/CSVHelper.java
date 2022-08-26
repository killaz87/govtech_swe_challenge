package com.govtech.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.govtech.repository.bean.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERS = { "Name", "Salary"};
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public static List<User> csvToUsers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<User> users = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                User user = User.builder().name(csvRecord.get("Name")).salary(Double.valueOf(csvRecord.get("Salary"))).build();
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static String csvValidationCheck(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            //Headers size check
            if(csvParser.getHeaderMap().size() > 2)
                return "Data in file should only consist of 2 columns of data only, header as Name and Salary, please try again.";

            //Headers checks
            if(csvParser.getHeaderMap().get("Name") == null || csvParser.getHeaderMap().get("Salary") == null)
                return "Header in the file must be Name and Salary, please try again.";

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                //If csv record has any more than 2 columns, fail the validation check
                if(csvRecord.size() > 2)
                    return "Data in file should only consist of 2 columns of data only, header as Name and Salary, please try again.";

                if(csvRecord.get("Name") == null || csvRecord.get("Name").equalsIgnoreCase("")
                        || csvRecord.get("Salary") == null || csvRecord.get("Salary").equalsIgnoreCase(""))
                    return "There is empty data in the file, please try again.";
            }
            return "";
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}