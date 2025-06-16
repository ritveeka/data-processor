package com.example.file_size_processor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LongSummaryStatistics;

public class FileSizeAggregator {
    private static final double BYTES_PER_MEGABYTE = 1024.0 * 1024.0;
    private static final String CREATOR_PREFIX = "creator_";
    private static final String CLIENT_PREFIX = "client_";
    private static final String INPUT_FILENAME = "video_usage_report_complex.csv";
    private static final String OUTPUT_FILENAME = "video_file_aggregated_size.csv";

    public static void main(String[] args) {
        try {
            // Read and Parse the Input CSV.
            List<FileRecord> videoFiles = CsvHandler.readFromCsv(INPUT_FILENAME);

            // Group and Compute Statistics using Java Streams.
            Map<String, LongSummaryStatistics> analyticsMap = videoFiles.stream()
                .collect(Collectors.groupingBy(
                    // The function to determine the group key for each video file
                    videoFile -> createGroupKeyFromPath(videoFile.getFilePath()),
                    // The downstream collector to perform the aggregation on each group
                    Collectors.summarizingLong(videoFile -> videoFile.getFileSize() / BYTES_PER_MEGABYTE)
                ));

            // Write the aggregated results to the output CSV.
            CsvHandler.writeToCsv(analyticsMap, OUTPUT_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createGroupKeyFromPath(String filePath) {
        Path path = Paths.get(filePath);
        String name;
        
        // A path can have both creator and client prefixes.
        // We find the first prefix that matches and return the client or creator name.
        for (Path part : path) {
            String partStr = part.toString();
            if (partStr.startsWith(CREATOR_PREFIX)) {
                name = partStr.substring(CREATOR_PREFIX.length());
                return name;
            } else if (partStr.startsWith(CLIENT_PREFIX)) {
                name = partStr.substring(CLIENT_PREFIX.length());
                return name;
            }
        }
    }
}