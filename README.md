# SysVerge – System Report Generator in Java

**SysVerge** is a lightweight Java-based system report generator that gathers system information using the [OSHI](https://github.com/oshi/oshi) (Operating System and Hardware Information) library. It writes a timestamped report with CPU, memory, disk, and filesystem details.

---

## Features

* CPU and processor information
* Total and available memory
* Disk and filesystem usage
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

Windows (PowerShell):

```powershell
mvn clean package
```

This will generate a fat JAR with dependencies at:

```
target/sysverge-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

### Run Instructions

After building the project, run it using the following command:

```bash
java -jar target/sysverge-1.0-SNAPSHOT-jar-with-dependencies.jar

```

Windows (PowerShell):

```powershell
java -jar target\sysverge-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Windows (Command Prompt):

```bat
java -jar target\sysverge-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Optional flags:

* `--output-dir <dir>`: directory where the report is written (default: current dir)
* `--sample-ms <ms>`: CPU load sampling interval in milliseconds (default: 1000)

Example output:

```
Operating System
----------------
Family: Linux
Version: 6.2.0-39-generic

CPU Information
---------------
Name: Intel(R) Core(TM) i7-10750H CPU @ 2.60GHz
Logical processors: 12
System CPU Load: 8.1%
```

---

## Tech Stack

* Java 17
* Maven
* OSHI (Operating System and Hardware Information)

---

## License

This project is licensed under the [MIT License](LICENSE).

