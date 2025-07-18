# SysVerge – System Analyzer in Java

**SysVerge** is a lightweight Java-based system analyzer that gathers and displays real-time system information using the [OSHI](https://github.com/oshi/oshi) (Operating System and Hardware Information) library. It reports details such as CPU usage, memory statistics, disk usage, and system uptime.

---

## Features

* CPU and processor information
* Total and available memory
* Disk space usage
* System boot time and uptime
* Cross-platform support (Windows, macOS, Linux)

---

## Getting Started

### Prerequisites

* Java 17 or newer
* Maven 3.x

---

### Build Instructions

1. Clone the repository:

```bash
git clone https://github.com/sedegah/SysVerge.git
cd SysVerge
```

2. Build the project and create a runnable JAR:

```bash
mvn clean package
```

This will generate a fat JAR with dependencies at:

```
target/SystemAnalyzer-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

### Run Instructions

After building the project, run it using the following command:

```bash
java -jar target/SystemAnalyzer-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Example output:

```
===== SYSTEM ANALYSIS =====
CPU: Intel(R) Core(TM) i7-10750H CPU @ 2.60GHz
Available Processors: 12
Memory: 16 GB
Free Memory: 5.7 GB
Disk: 475.9 GB free of 953.9 GB
System Boot Time: 2025-07-18 08:12:44
System Uptime: 1 hours, 32 minutes, 14 seconds
===========================
```

---

## Tech Stack

* Java 17
* Maven
* OSHI (Operating System and Hardware Information)

---

## License

This project is licensed under the [MIT License](LICENSE).

