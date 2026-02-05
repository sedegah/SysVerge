package com.sysverge;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ReportWriter {
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private ReportWriter() {
    }

    public static String defaultFilename(LocalDateTime timestamp) {
        return "sysreport_" + timestamp.format(FILE_TIMESTAMP) + ".txt";
    }

    public static Path writeReport(String report, Path outputDir, String filename) throws IOException {
        Path normalizedDir = outputDir.toAbsolutePath().normalize();
        Files.createDirectories(normalizedDir);

        Path outputFile = normalizedDir.resolve(filename);
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            writer.write(report);
        }

        return outputFile;
    }
}
