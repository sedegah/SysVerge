package com.sysverge;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        ReportOptions options;
        try {
            options = ReportOptions.fromArgs(args);
            if (options == null) {
                ReportOptions.printUsage();
                return;
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            ReportOptions.printUsage();
            System.exit(2);
            return;
        }

        SystemReportBuilder builder = new SystemReportBuilder();
        String report = builder.buildReport(options);
        String filename = ReportWriter.defaultFilename(LocalDateTime.now());

        try {
            Path outputFile = ReportWriter.writeReport(report, options.outputDir(), filename);
            System.out.println("System report saved to: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error writing system report: " + e.getMessage());
            System.exit(1);
        }
    }
}
