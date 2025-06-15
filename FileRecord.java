package com.example.file_size_processor;

public class FileRecord {
    String filePath;
    String fileSize;

    FileRecord(String filePath, String fileSize) {
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    String setFilePath(String filePath) {
        this.filePath = filePath;
    }

    String setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    String getFilePath() {
        return this.filePath;
    }

    String getFileSize() {
        return this.fileSize;
    }
}