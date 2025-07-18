package com.monitor;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.HWDiskStore;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        StringBuilder sb = new StringBuilder();

        appendOSInfo(sb, os);
        appendCpuInfo(sb, hal.getProcessor());
        appendMemoryInfo(sb, hal.getMemory());
        appendDiskInfo(sb, hal.getDiskStores());
        appendFileSystemInfo(sb, os.getFileSystem());

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = "sysreport_" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(sb.toString());
            System.out.println("System report saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing system report: " + e.getMessage());
        }
    }

    private static void appendOSInfo(StringBuilder sb, OperatingSystem os) {
        sb.append("Operating System\n").append("-".repeat(20)).append("\n");
        sb.append("Family: ").append(os.getFamily()).append("\n");
        sb.append("Version: ").append(os.getVersionInfo().toString()).append("\n\n");
    }

    private static void appendCpuInfo(StringBuilder sb, CentralProcessor cpu) {
        sb.append("CPU Information\n").append("-".repeat(20)).append("\n");
        sb.append("Name: ").append(cpu.getProcessorIdentifier().getName()).append("\n");
        sb.append("Identifier: ").append(cpu.getProcessorIdentifier().getIdentifier()).append("\n");
        sb.append("Microarchitecture: ").append(cpu.getProcessorIdentifier().getMicroarchitecture()).append("\n");
        sb.append("Physical CPU packages: ").append(cpu.getPhysicalPackageCount()).append("\n");
        sb.append("Physical cores: ").append(cpu.getPhysicalProcessorCount()).append("\n");
        sb.append("Logical processors: ").append(cpu.getLogicalProcessorCount()).append("\n");
        sb.append("Vendor Frequency: ").append(FormatUtil.formatHertz(cpu.getProcessorIdentifier().getVendorFreq())).append("\n");

        long[] freq = cpu.getCurrentFreq();
        if (freq.length > 0) {
            sb.append("Current Frequencies (per core):\n");
            for (int i = 0; i < freq.length; i++) {
                sb.append("  Core ").append(i).append(": ").append(FormatUtil.formatHertz(freq[i])).append("\n");
            }
        }

        long[] prevSysTicks = cpu.getSystemCpuLoadTicks();
        long[][] prevProcTicks = cpu.getProcessorCpuLoadTicks();

        Util.sleep(1000);

        long[] currSysTicks = cpu.getSystemCpuLoadTicks();
        double systemCpuLoad = cpu.getSystemCpuLoadBetweenTicks(prevSysTicks);
        sb.append(String.format("System CPU Load: %.1f%%\n", systemCpuLoad * 100));

        double[] load = cpu.getProcessorCpuLoadBetweenTicks(prevProcTicks);
        sb.append("CPU Load (per core):\n");
        for (int i = 0; i < load.length; i++) {
            sb.append("  Core ").append(i).append(": ").append(String.format("%.1f%%", load[i] * 100)).append("\n");
        }

        sb.append("\n");
    }

    private static void appendMemoryInfo(StringBuilder sb, GlobalMemory memory) {
        sb.append("Memory Information\n").append("-".repeat(20)).append("\n");
        sb.append("Total Memory: ").append(FormatUtil.formatBytes(memory.getTotal())).append("\n");
        sb.append("Available Memory: ").append(FormatUtil.formatBytes(memory.getAvailable())).append("\n");
        sb.append("Used Memory: ").append(FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable())).append("\n\n");
    }

    private static void appendDiskInfo(StringBuilder sb, List<HWDiskStore> diskStores) {
        sb.append("Disk Information\n").append("-".repeat(20)).append("\n");
        for (HWDiskStore disk : diskStores) {
            sb.append("Disk: ").append(disk.getName()).append("\n");
            sb.append("Model: ").append(disk.getModel()).append("\n");
            sb.append("Serial: ").append(disk.getSerial()).append("\n");
            sb.append("Size: ").append(FormatUtil.formatBytesDecimal(disk.getSize())).append("\n\n");
        }
    }

    private static void appendFileSystemInfo(StringBuilder sb, FileSystem fs) {
        sb.append("File System Information\n").append("-".repeat(20)).append("\n");
        for (OSFileStore store : fs.getFileStores()) {
            sb.append("Mount: ").append(store.getMount()).append("\n");
            sb.append("Name: ").append(store.getName()).append("\n");
            sb.append("Type: ").append(store.getType()).append("\n");
            sb.append("Total Space: ").append(FormatUtil.formatBytes(store.getTotalSpace())).append("\n");
            sb.append("Usable Space: ").append(FormatUtil.formatBytes(store.getUsableSpace())).append("\n\n");
        }
    }
}
