
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EvaluationReport {

    public void generate(String rollNo) throws SQLException {

        String sql = """
            SELECT s.roll_no, s.student_name,
                   c.course_code, c.course_title, c.course_type,
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

                    System.out.println("\n========================================");
                    System.out.println("       KEC EVALUATION REPORT");
                    System.out.println("========================================");
                    System.out.println("Roll No: " + rs.getString("roll_no"));
                    System.out.println("Student: " + rs.getString("student_name"));
                    System.out.println("Course Code: " + rs.getString("course_code"));
                    System.out.println("Course: " + rs.getString("course_title"));
                    System.out.println("Course Type: " + rs.getString("course_type"));

                    int totalClasses = rs.getInt("total_classes");
                    int attended = rs.getInt("classes_attended");

                    double attendance = totalClasses > 0
                            ? attended * 100.0 / totalClasses : 0.0;

                    System.out.printf("Attendance: %.2f%%%n", attendance);

                    // Do not calculate a final grade from incomplete marks.
                    Object cia1Obj = rs.getObject("cia1");
                    Object cia2Obj = rs.getObject("cia2");
                    Object assignmentObj = rs.getObject("assignment_quiz_lab");
                    Object eseObj = rs.getObject("ese_marks");

                    boolean absent = rs.getBoolean("ese_absent");

                    if (cia1Obj == null || cia2Obj == null
                            || assignmentObj == null
                            || (eseObj == null && !absent)) {
                        System.out.println("CIA 1: " + display(cia1Obj));
                        System.out.println("CIA 2: " + display(cia2Obj));
                        System.out.println("Assignment/Quiz/Lab: "
                                + display(assignmentObj));
                        System.out.println("ESE: "
                                + (absent ? "AB" : display(eseObj)));
                        System.out.println("Final Grade: PENDING");
                        System.out.println("Status: Assessment marks incomplete");
                        continue;
                    }

                    double cia1 = ((Number) cia1Obj).doubleValue();
                    double cia2 = ((Number) cia2Obj).doubleValue();
                    double assignment =
                            ((Number) assignmentObj).doubleValue();
                    double ese = eseObj == null ? 0.0
                            : ((Number) eseObj).doubleValue();

                    EvaluationEngine.EvaluationResult result =
                            EvaluationEngine.evaluate(
                                    rs.getString("course_type"),
                                    cia1, cia2, assignment, ese, absent,
                                    attendance, false);

                    System.out.printf("CIA Average: %.2f%n", result.ciaScore);
                    System.out.printf("Weighted CIA: %.2f%n", result.weightedCIA);
                    System.out.printf("Weighted ESE: %.2f%n", result.weightedESE);
                    System.out.printf("Total Marks: %.2f%n", result.totalMarks);
                    System.out.println("Grade: " + result.grade);
                    System.out.println("Grade Point: " + result.gradePoint);
                    System.out.println("Status: " + result.status);
                    System.out.println("========================================");
                }

                if (!found) {
                    System.out.println("No enrolled courses found for " + rollNo);
                }
            }
        }
    }

    private String display(Object value) {
        return value == null ? "Not entered" : value.toString();
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter student roll number: ");
            String rollNo = sc.nextLine().trim();

            try {
                new EvaluationReport().generate(rollNo);
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }
}
