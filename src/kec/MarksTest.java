
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarksTest {
    public static void main(String[] args) {
        String sql = """
            SELECT s.roll_no, s.student_name, c.course_title,
                   a.cia1, a.cia2, a.assignment_quiz_lab,
                   a.ese_marks, a.ese_absent
            FROM assessment_marks a
            JOIN enrollments e ON a.enrollment_id = e.enrollment_id
            JOIN students s ON e.roll_no = s.roll_no
            JOIN courses c ON e.course_code = c.course_code
            WHERE s.roll_no = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "25ITR182");

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("\n--- Assessment Marks ---");
                    System.out.println("Roll No: " + rs.getString("roll_no"));
                    System.out.println("Student: " + rs.getString("student_name"));
                    System.out.println("Course: " + rs.getString("course_title"));
                    System.out.println("CIA 1: " + rs.getDouble("cia1"));
                    System.out.println("CIA 2: " + rs.getDouble("cia2"));
                    System.out.println("Assignment/Quiz/Lab: "
                            + rs.getDouble("assignment_quiz_lab"));
                    System.out.println("ESE Marks: " + rs.getDouble("ese_marks"));
                    System.out.println("ESE Absent: "
                            + (rs.getBoolean("ese_absent") ? "Yes" : "No"));
                }

                if (!found) {
                    System.out.println("No assessment records found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
