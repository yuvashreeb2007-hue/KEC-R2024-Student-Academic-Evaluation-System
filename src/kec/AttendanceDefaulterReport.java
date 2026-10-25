
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendanceDefaulterReport {

    public void generate() throws SQLException {

        String sql = """
            SELECT s.roll_no, s.student_name,
                   c.course_code, c.course_title,
                   e.total_classes, e.classes_attended
            FROM students s
            JOIN enrollments e ON s.roll_no = e.roll_no
            JOIN courses c ON e.course_code = c.course_code
            WHERE e.total_classes > 0
              AND (e.classes_attended * 100.0 / e.total_classes) < 65
            ORDER BY s.roll_no, c.course_code
            """;

        boolean found = false;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n==============================================");
            System.out.println("       KEC ATTENDANCE DEFAULTER REPORT");
            System.out.println("==============================================");

            while (rs.next()) {
                found = true;

                int total = rs.getInt("total_classes");
                int attended = rs.getInt("classes_attended");
                double percentage = attended * 100.0 / total;

                System.out.println("\nRoll Number : "
                        + rs.getString("roll_no"));
                System.out.println("Student Name: "
                        + rs.getString("student_name"));
                System.out.println("Course      : "
                        + rs.getString("course_code") + " - "
                        + rs.getString("course_title"));
                System.out.println("Attendance  : "
                        + String.format("%.2f%%", percentage));
                System.out.println("Status      : SA");
                System.out.println("----------------------------------------------");
            }

            if (!found) {
                System.out.println(
                        "No students have attendance below 65%.");
            }

            System.out.println("==============================================");
        }
    }

    public static void main(String[] args) {
        try {
            new AttendanceDefaulterReport().generate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
