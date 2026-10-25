# Kongu Engineering College (Autonomous)
## Student Academic & Continuous Evaluation System — R2024

### 1. Project Objective
To develop a database-driven Java application using JDBC and MariaDB to manage student records, courses, attendance, continuous internal assessment marks, End Semester Examination marks, academic evaluation, and performance reports.

### 2. Technologies Used
- Programming Language: Java
- Database: MariaDB
- Database Connectivity: JDBC
- JDBC Driver: MariaDB Java Client
- Development Environment: Fedora Linux
- Java Compiler: `javac`

### 3. Database Tables
1. `students` — stores student registration details.
2. `courses` — stores course codes, titles, credits, and course types.
3. `enrollments` — stores student-course enrollment and attendance.
4. `assessment_marks` — stores CIA, assignment/quiz/lab, ESE marks, and ESE absence status.

### 4. Main Application Features
- Add and view student records.
- Add and view courses.
- View and update attendance.
- Update individual assessment marks.
- Generate academic performance reports.
- Calculate final grades using the configured evaluation rules.
- Generate SGPA and equivalent percentage reports.
- Generate attendance defaulter reports.
- Generate department-wise class performance summaries.

### 5. Attendance Eligibility Rules
- Attendance of 75% or more: eligible for ESE.
- Attendance from 65% to below 75%: conditional eligibility, subject to medical condonation approval.
- Attendance below 65%: classified as SA according to the implemented rules.

### 6. Evaluation Rules
The CIA score is calculated as:

CIA = (CIA Test 1 + CIA Test 2 + Assignment/Quiz/Lab) / 3

Weighted CIA and ESE marks are calculated using the configured weights for THEORY, PRACTICAL, and EMBEDDED courses. ESE minimum marks, overall pass marks, grades, and grade points are determined by the evaluation rules implemented in the application.

### 7. SGPA Calculation
SGPA = Sum of (Course Credits × Grade Points) / Sum of Course Credits

Equivalent Percentage = SGPA × 10

### 8. Compilation
```bash
javac -cp /usr/lib/java/mariadb-java-client.jar -d out src/kec/*.java
```

### 9. Running the Main Application
```bash
java -cp "out:/usr/lib/java/mariadb-java-client.jar" kec.MainApp
```

### 10. Database Schema Export
```bash
sudo mariadb-dump --no-data kec_academic > kec_schema.sql
```

### 11. Testing
The application should be tested for student and course management, attendance updates, incremental marks entry, ESE absence, attendance eligibility, grade calculation, SGPA generation, and class performance reporting.

### 12. Conclusion
The application integrates student academic records, attendance tracking, assessment entry, evaluation, and reporting through Java JDBC and MariaDB. It provides a foundation for managing continuous evaluation and semester academic results.
