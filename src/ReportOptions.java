package com.sysverge;

import java.nio.file.Path;
import java.time.Duration;

public final class ReportOptions {
    private static final Duration DEFAULT_SAMPLE_INTERVAL = Duration.ofSeconds(1);
    private static final Path DEFAULT_OUTPUT_DIR = Path.of(".");

    private final Duration sampleInterval;
    private final Path outputDir;

    private ReportOptions(Duration sampleInterval, Path outputDir) {
        this.sampleInterval = sampleInterval;
        this.outputDir = outputDir;
    }

    public Duration sampleInterval() {
        return sampleInterval;
    }

    public Path outputDir() {
        return outputDir;
    }

    public static ReportOptions fromArgs(String[] args) {
        if (args == null || args.length == 0) {
            return defaults();
        }

        Duration sampleInterval = DEFAULT_SAMPLE_INTERVAL;
        Path outputDir = DEFAULT_OUTPUT_DIR;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--help".equals(arg) || "-h".equals(arg)) {
                return null;
            }
            if ("--output-dir".equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for --output-dir");
                }
                outputDir = Path.of(args[++i]);
                continue;
            }
            if ("--sample-ms".equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for --sample-ms");
                }
                String value = args[++i];
                long millis;
                try {
                    millis = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid --sample-ms value: " + value);
                }
                if (millis < 0) {
                    throw new IllegalArgumentException("--sample-ms must be >= 0");
                }
                sampleInterval = Duration.ofMillis(millis);
                continue;
            }

            throw new IllegalArgumentException("Unknown argument: " + arg);
        }

        return new ReportOptions(sampleInterval, outputDir);
    }

    public static void printUsage() {
        System.out.println("Usage: java -jar <jar> [--output-dir <dir>] [--sample-ms <ms>] [--help]");
        System.out.println("  --output-dir <dir>  Directory to write the report (default: current dir)");
        System.out.println("  --sample-ms <ms>    CPU load sampling interval in ms (default: 1000)");
    }

    private static ReportOptions defaults() {
        return new ReportOptions(DEFAULT_SAMPLE_INTERVAL, DEFAULT_OUTPUT_DIR);
    }
}
