package com.monitor;

import java.io.FileWriter;
import java.io.IOException;

public class SystemExporter {
    public static void exportToTxt(String data, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(data);
        }
    }
}
