
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicDAO {

    public void viewAttendance(String rollNo) throws SQLException {
        String sql = """
            SELECT s.roll_no, s.student_name, c.course_title,
                   e.total_classes, e.classes_attended
            FROM enrollments e
            JOIN students s ON e.roll_no = s.roll_no
            JOIN courses c ON e.course_code = c.course_code
            WHERE s.roll_no = ?
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

                    double percentage = total > 0
                            ? attended * 100.0 / total : 0.0;

                    System.out.println("\n--- Attendance Details ---");
                    System.out.println("Roll No: " + rs.getString("roll_no"));
                    System.out.println("Student: " + rs.getString("student_name"));
                    System.out.println("Course: " + rs.getString("course_title"));
                    System.out.println("Total Classes: " + total);
                    System.out.println("Classes Attended: " + attended);
                    System.out.printf("Attendance: %.2f%%%n", percentage);
                }

                if (!found) {
                    System.out.println("No enrollment found for roll number: " + rollNo);
                }
            }
        }
    }

    public void updateAttendance(String rollNo, String courseCode,
                                 int totalClasses, int classesAttended)
            throws SQLException {

        if (totalClasses < 0 || classesAttended < 0
                || classesAttended > totalClasses) {
            System.out.println("Invalid attendance values.");
            return;
        }

        String sql = """
            UPDATE enrollments
            SET total_classes = ?, classes_attended = ?
            WHERE roll_no = ? AND course_code = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, totalClasses);
            ps.setInt(2, classesAttended);
            ps.setString(3, rollNo);
            ps.setString(4, courseCode);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Attendance updated successfully!");
            } else {
                System.out.println("Enrollment not found.");
            }
        }
    }

    public void updateMarks(int enrollmentId, double cia1, double cia2,
                            double assignmentQuizLab, double eseMarks)
            throws SQLException {

        if (cia1 < 0 || cia1 > 100
                || cia2 < 0 || cia2 > 100
                || assignmentQuizLab < 0 || assignmentQuizLab > 100
                || eseMarks < 0 || eseMarks > 100) {
            System.out.println("Marks must be between 0 and 100.");
            return;
        }

        String sql = """
            UPDATE assessment_marks
            SET cia1 = ?, cia2 = ?, assignment_quiz_lab = ?, ese_marks = ?
            WHERE enrollment_id = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, cia1);
            ps.setDouble(2, cia2);
            ps.setDouble(3, assignmentQuizLab);
            ps.setDouble(4, eseMarks);
            ps.setInt(5, enrollmentId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Assessment marks updated successfully!");
            } else {
                System.out.println("Assessment record not found.");
            }
        }
    }

public void updateSingleMark(int enrollmentId, String assessment,
                             double marks) throws SQLException {

    if (marks < 0 || marks > 100) {
        System.out.println("Marks must be between 0 and 100.");
        return;
    }

    String column;

    switch (assessment.toUpperCase()) {
        case "CIA1":
            column = "cia1";
            break;
        case "CIA2":
            column = "cia2";
            break;
        case "ASSIGNMENT":
            column = "assignment_quiz_lab";
            break;
        case "ESE":
            column = "ese_marks";
            break;
        default:
            System.out.println("Invalid assessment type.");
            return;
    }

    String sql = "UPDATE assessment_marks SET " + column
            + " = ? WHERE enrollment_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDouble(1, marks);
        ps.setInt(2, enrollmentId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println(assessment + " marks updated successfully!");
        } else {
            System.out.println("Assessment record not found.");
        }
    }
}

}
