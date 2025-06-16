package com.example.file_size_processor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LongSummaryStatistics;

public class CsvHandler {
    public static List<FileRecord> readFromCsv(String inputFilePath) throws IOException {
        List<FileRecord> records = new ArrayList<>();
        try (Reader reader = new FileReader(inputFilePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                String filePath = csvRecord.get("file_path");
                long fileSize = Long.parseLong(csvRecord.get("file_size_bytes"));
                records.add(new FileRecord(filePath, fileSize));
            }
        }
        return records;
    }

    public static void writeToCsv(Map<String, LongSummaryStatistics> analyticsMap, String outputFilePath) throws IOException {
        // Define output headers
        String[] headers = {"creator or client name", "totalFileSize (MB)", "fileSizeMin (MB)", "fileSizeAverage (MB)"};

        try (Writer writer = new FileWriter(outputFilePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {

            // Iterate through the map and write each entry as a row
            for (Map.Entry<String, LongSummaryStatistics> entry : analyticsMap.entrySet()) {
                String key = entry.getKey();
                LongSummaryStatistics stats = entry.getValue();

                // Write to CSV file
                csvPrinter.printRecord(
                    key.name(),
                    String.format("%d", stats.getSum()),
                    String.format("%d", stats.getMin()),
                    String.format("%d", stats.getAverage())
                );
            }
        }
    }
}