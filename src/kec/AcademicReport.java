
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicReport {

    public void generateReport(String rollNo) throws SQLException {

        String sql = """
            SELECT s.roll_no, s.student_name, s.department,
                   s.current_semester, c.course_code, c.course_title,
                   e.total_classes, e.classes_attended,
                   a.cia1, a.cia2, a.assignment_quiz_lab,
                   a.ese_marks, a.ese_absent
            FROM students s
            JOIN enrollments e ON s.roll_no = e.roll_no
            JOIN courses c ON e.course_code = c.course_code
            LEFT JOIN assessment_marks a
                   ON e.enrollment_id = a.enrollment_id
            WHERE s.roll_no = ?
            ORDER BY c.course_code
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;

                    int total = rs.getInt("total_classes");
                    int attended = rs.getInt("classes_attended");

                    double attendance = total > 0
                            ? attended * 100.0 / total : 0.0;

                    System.out.println("\n====================================");
                    System.out.println(" KEC ACADEMIC PERFORMANCE REPORT");
                    System.out.println("====================================");
                    System.out.println("Roll No: " + rs.getString("roll_no"));
                    System.out.println("Name: " + rs.getString("student_name"));
                    System.out.println("Department: " + rs.getString("department"));
                    System.out.println("Semester: " + rs.getInt("current_semester"));
                    System.out.println("------------------------------------");
                    System.out.println("Course Code: " + rs.getString("course_code"));
                    System.out.println("Course: " + rs.getString("course_title"));
                    System.out.println("Attendance: "
                            + attended + "/" + total);
                    System.out.printf("Attendance Percentage: %.2f%%%n", attendance);

                    Object cia1 = rs.getObject("cia1");
                    Object cia2 = rs.getObject("cia2");
                    Object assignment = rs.getObject("assignment_quiz_lab");
                    Object ese = rs.getObject("ese_marks");

                    System.out.println("------------------------------------");
                    System.out.println("CIA 1: " + (cia1 == null ? "Not entered" : cia1));
                    System.out.println("CIA 2: " + (cia2 == null ? "Not entered" : cia2));
                    System.out.println("Assignment/Quiz/Lab: "
                            + (assignment == null ? "Not entered" : assignment));
                    System.out.println("ESE Marks: " + (ese == null ? "Not entered" : ese));
                    System.out.println("ESE Absent: "
                            + (rs.getBoolean("ese_absent") ? "Yes" : "No"));
                    System.out.println("====================================");
                }

                if (!found) {
                    System.out.println("No academic records found for " + rollNo);
                }
            }
        }
    }

    public static void main(String[] args) {
        AcademicReport report = new AcademicReport();

        try (java.util.Scanner sc = new java.util.Scanner(System.in)) {
            System.out.print("Enter student roll number: ");
            String rollNo = sc.nextLine().trim();
            report.generateReport(rollNo);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
