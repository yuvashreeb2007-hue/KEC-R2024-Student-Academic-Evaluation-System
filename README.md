# KEC R2024 Student Academic & Continuous Evaluation System

## Project Overview

The **KEC R2024 Student Academic & Continuous Evaluation System** is a database-driven Java application developed using Java JDBC and MariaDB. The system is designed to manage student academic information, course details, attendance records, assessment marks, and academic performance reports according to Kongu Engineering College Autonomous Regulation 2024.

## Objectives

- To maintain student and course information efficiently.
- To manage course enrollment and attendance records.
- To record Continuous Internal Assessment (CIA) and End Semester Examination (ESE) marks.
- To support attendance eligibility checking.
- To generate academic performance reports for students.

## Technologies Used

- **Programming Language:** Java
- **Database:** MariaDB
- **Database Connectivity:** JDBC
- **Database Driver:** MariaDB JDBC Client
- **Development Environment:** Fedora Linux
- **Java Version:** OpenJDK 25

## Features

### 1. Student Management
Maintains student details, including roll number, student name, department, and current semester.

### 2. Course Management
Stores course information, including course code, course name, credits, and course type.

### 3. Attendance Management
Records the total number of classes conducted and classes attended. Attendance percentage can be calculated to help determine attendance eligibility.

### 4. Assessment Marks Management
Stores and updates CIA marks, assignment/quiz/laboratory marks, and End Semester Examination marks.

### 5. Academic Performance Reports
Generates academic performance reports using student details, attendance information, and assessment marks.

## Database Design

The application uses the `kec_academic` database with the following tables:

| Table | Purpose |
|---|---|
| `students` | Stores student information |
| `courses` | Stores course information |
| `enrollments` | Stores enrollment and attendance details |
| `assessment_marks` | Stores assessment and examination marks |

## Project Structure

```text
KECAcademicSystem/
├── src/
│   └── kec/
│       ├── DBConnection.java
│       ├── DBConnectionTest.java
│       ├── StudentDAO.java
│       ├── StudentDAOTest.java
│       └── MainApp.java
├── kec_schema.sql
├── .gitignore
└── README.md
```

*Note: The file list above reflects the expected project layout. Update it if your actual files differ.*

## Prerequisites

Before running the application, install or configure:

- Java Development Kit (JDK)
- MariaDB Server
- MariaDB JDBC driver

## Setup and Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/yuvashreeb2007-hue/KEC-R2024-Student-Academic-Evaluation-System.git
cd KEC-R2024-Student-Academic-Evaluation-System
```

### Step 2: Configure the Database

Create the database in MariaDB:

```sql
CREATE DATABASE kec_academic;
```

Import the database schema if `kec_schema.sql` contains the required table definitions:

```bash
mariadb -u root -p kec_academic < kec_schema.sql
```

If the database and tables already exist, do not import the schema again without checking for conflicts.

### Step 3: Configure Database Connectivity

Open `DBConnection.java` and configure the JDBC URL, database username, and password for your local MariaDB setup.

**Security note:** Never commit database passwords or other secret credentials to a public repository.

### Step 4: Compile the Java Application

On Fedora, if the MariaDB JDBC driver is installed at `/usr/lib/java/mariadb-java-client.jar`, compile the source files using:

```bash
javac -cp /usr/lib/java/mariadb-java-client.jar -d out src/kec/*.java
```

### Step 5: Run the Application

```bash
java -cp "out:/usr/lib/java/mariadb-java-client.jar" kec.MainApp
```

Ensure that the database is running and the JDBC connection settings are correct before starting the application.

## Academic Use

This project is developed for academic purposes to demonstrate Java programming, JDBC connectivity, relational database management, attendance tracking, assessment management, and report generation.

## Author

**Yuvashree B**

GitHub: [yuvashreeb2007-hue](https://github.com/yuvashreeb2007-hue)

## License

This project is intended for educational use. Add an appropriate open-source license if you wish to permit reuse or redistribution under specific terms.
