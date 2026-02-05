package com.sysverge;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.HWDiskStore;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.time.Duration;
import java.util.List;

public final class SystemReportBuilder {
    private final SystemInfo systemInfo;

    public SystemReportBuilder() {
        this(new SystemInfo());
    }

    SystemReportBuilder(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public String buildReport(ReportOptions options) {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        StringBuilder sb = new StringBuilder(2048);

        appendOsInfo(sb, os);
        appendCpuInfo(sb, hal.getProcessor(), options.sampleInterval());
        appendMemoryInfo(sb, hal.getMemory());
        appendDiskInfo(sb, hal.getDiskStores());
        appendFileSystemInfo(sb, os.getFileSystem());

        return sb.toString();
    }

    private static void appendOsInfo(StringBuilder sb, OperatingSystem os) {
        appendSectionHeader(sb, "Operating System");
        sb.append("Family: ").append(os.getFamily()).append('\n');
        sb.append("Version: ").append(os.getVersionInfo().toString()).append("\n\n");
    }

    private static void appendCpuInfo(StringBuilder sb, CentralProcessor cpu, Duration sampleInterval) {
        appendSectionHeader(sb, "CPU Information");
        CentralProcessor.ProcessorIdentifier id = cpu.getProcessorIdentifier();
        sb.append("Name: ").append(id.getName()).append('\n');
        sb.append("Identifier: ").append(id.getIdentifier()).append('\n');
        sb.append("Microarchitecture: ").append(id.getMicroarchitecture()).append('\n');
        sb.append("Physical CPU packages: ").append(cpu.getPhysicalPackageCount()).append('\n');
        sb.append("Physical cores: ").append(cpu.getPhysicalProcessorCount()).append('\n');
        sb.append("Logical processors: ").append(cpu.getLogicalProcessorCount()).append('\n');
        sb.append("Vendor Frequency: ").append(FormatUtil.formatHertz(id.getVendorFreq())).append("\n");

        long[] freq = cpu.getCurrentFreq();
        if (freq != null && freq.length > 0) {
            sb.append("Current Frequencies (per core):\n");
            for (int i = 0; i < freq.length; i++) {
                sb.append("  Core ").append(i).append(": ").append(FormatUtil.formatHertz(freq[i])).append('\n');
            }
        }

        long[] prevSysTicks = cpu.getSystemCpuLoadTicks();
        long[][] prevProcTicks = cpu.getProcessorCpuLoadTicks();
        sleep(sampleInterval);

        double systemCpuLoad = cpu.getSystemCpuLoadBetweenTicks(prevSysTicks);
        sb.append(String.format("System CPU Load: %.1f%%\n", systemCpuLoad * 100));

        double[] load = cpu.getProcessorCpuLoadBetweenTicks(prevProcTicks);
        sb.append("CPU Load (per core):\n");
        for (int i = 0; i < load.length; i++) {
            sb.append("  Core ").append(i).append(": ")
                    .append(String.format("%.1f%%", load[i] * 100))
                    .append('\n');
        }

        sb.append('\n');
    }

    private static void appendMemoryInfo(StringBuilder sb, GlobalMemory memory) {
        appendSectionHeader(sb, "Memory Information");
        long total = memory.getTotal();
        long available = memory.getAvailable();
        sb.append("Total Memory: ").append(FormatUtil.formatBytes(total)).append('\n');
        sb.append("Available Memory: ").append(FormatUtil.formatBytes(available)).append('\n');
        sb.append("Used Memory: ").append(FormatUtil.formatBytes(total - available)).append("\n\n");
    }

    private static void appendDiskInfo(StringBuilder sb, List<HWDiskStore> diskStores) {
        appendSectionHeader(sb, "Disk Information");
        for (HWDiskStore disk : diskStores) {
            sb.append("Disk: ").append(disk.getName()).append('\n');
            sb.append("Model: ").append(disk.getModel()).append('\n');
            sb.append("Serial: ").append(disk.getSerial()).append('\n');
            sb.append("Size: ").append(FormatUtil.formatBytesDecimal(disk.getSize())).append("\n\n");
        }
    }

    private static void appendFileSystemInfo(StringBuilder sb, FileSystem fs) {
        appendSectionHeader(sb, "File System Information");
        for (OSFileStore store : fs.getFileStores()) {
            sb.append("Mount: ").append(store.getMount()).append('\n');
            sb.append("Name: ").append(store.getName()).append('\n');
            sb.append("Type: ").append(store.getType()).append('\n');
            sb.append("Total Space: ").append(FormatUtil.formatBytes(store.getTotalSpace())).append('\n');
            sb.append("Usable Space: ").append(FormatUtil.formatBytes(store.getUsableSpace())).append("\n\n");
        }
    }

    private static void appendSectionHeader(StringBuilder sb, String title) {
        sb.append(title).append('\n');
        sb.append("-".repeat(title.length())).append('\n');
    }

    private static void sleep(Duration duration) {
        long millis = duration == null ? 0 : duration.toMillis();
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
