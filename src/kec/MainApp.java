
package kec;

import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDAO studentDAO = new StudentDAO();
        AcademicDAO academicDAO = new AcademicDAO();

        int choice = 0;

        do {
            System.out.println("\n======================================");
            System.out.println(" KEC STUDENT ACADEMIC SYSTEM");
            System.out.println("======================================");
            System.out.println("1. View Students");
            System.out.println("2. View Attendance");
            System.out.println("3. Update Attendance");
            System.out.println("4. Update Assessment Marks");
            System.out.println("5. View Courses");
System.out.println("6. Generate Academic Performance Report");
System.out.println("7. Update Individual Assessment Mark");
System.out.println("8. Generate SGPA Report");
System.out.println("9. Attendance Defaulter Report");
System.out.println("10. Class Performance Summary");
System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        studentDAO.viewStudents();
                        break;

                    case 2:
                        System.out.print("Enter student roll number: ");
                        String rollNo = sc.nextLine().trim();
                        academicDAO.viewAttendance(rollNo);
                        break;

                    case 3:
                        System.out.print("Enter student roll number: ");
                        String attendanceRoll = sc.nextLine().trim();

                        System.out.print("Enter course code: ");
                        String courseCode = sc.nextLine().trim();

                        System.out.print("Enter total classes: ");
                        int total = sc.nextInt();

                        System.out.print("Enter classes attended: ");
                        int attended = sc.nextInt();
                        sc.nextLine();

                        academicDAO.updateAttendance(
                                attendanceRoll, courseCode, total, attended);
                        break;

                    case 4:
                        System.out.print("Enter enrollment ID: ");
                        int enrollmentId = sc.nextInt();

                        System.out.print("Enter CIA 1 marks: ");
                        double cia1 = sc.nextDouble();

                        System.out.print("Enter CIA 2 marks: ");
                        double cia2 = sc.nextDouble();

                        System.out.print("Enter Assignment/Quiz/Lab marks: ");
                        double assignment = sc.nextDouble();

                        System.out.print("Enter ESE marks: ");
                        double ese = sc.nextDouble();
                        sc.nextLine();

                        academicDAO.updateMarks(
                                enrollmentId, cia1, cia2, assignment, ese);
                        break;

                    case 5:
    studentDAO.viewCourses();
    break;
case 6:
    System.out.print("Enter student roll number: ");
    String reportRoll = sc.nextLine().trim();

    AcademicReport report = new AcademicReport();
    report.generateReport(reportRoll);
    break;
case 7:
    System.out.print("Enter enrollment ID: ");
    int markEnrollmentId = sc.nextInt();
    sc.nextLine();

    System.out.println("Assessment types: CIA1, CIA2, ASSIGNMENT, ESE");
    System.out.print("Enter assessment type: ");
    String assessment = sc.nextLine().trim();

    System.out.print("Enter marks: ");
    double marks = sc.nextDouble();
    sc.nextLine();

    academicDAO.updateSingleMark(
            markEnrollmentId, assessment, marks);
    break;
case 8:
    System.out.print("Enter student roll number: ");
    rollNo = sc.nextLine().trim();

    try {
        new SGPAReport().generate(rollNo);
    } catch (java.sql.SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
    break;
case 9:
    try {
        new AttendanceDefaulterReport().generate();
    } catch (java.sql.SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
    break;
case 10:
    try {
        new ClassPerformanceReport().generate();
    } catch (java.sql.SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
    break;
case 11:
    System.out.println("Exiting KEC Academic System.");
    break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter the correct data type.");
                sc.nextLine();
            }

        } while (choice != 11);

        sc.close();
    }
}

