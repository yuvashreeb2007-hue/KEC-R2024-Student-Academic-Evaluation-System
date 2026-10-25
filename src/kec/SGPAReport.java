
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SGPAReport {

    public void generate(String rollNo) throws SQLException {

        String sql = """
            SELECT c.course_code, c.course_title, c.course_type, c.credits,
                   e.total_classes, e.classes_attended,
                   a.cia1, a.cia2, a.assignment_quiz_lab,
                   a.ese_marks, a.ese_absent
            FROM enrollments e
            JOIN courses c ON e.course_code = c.course_code
            LEFT JOIN assessment_marks a
                   ON e.enrollment_id = a.enrollment_id
            WHERE e.roll_no = ?
            ORDER BY c.course_code
            """;

        double totalCredits = 0.0;
        double weightedGradePoints = 0.0;
        boolean found = false;
        boolean pendingResult = false;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {

                System.out.println("\n========================================");
                System.out.println("       KEC SGPA REPORT");
                System.out.println("========================================");
                System.out.println("Roll Number: " + rollNo);

                while (rs.next()) {
                    found = true;

                    String courseCode = rs.getString("course_code");
                    String courseTitle = rs.getString("course_title");
                    String courseType = rs.getString("course_type");
                    double credits = rs.getDouble("credits");

                    Object cia1Obj = rs.getObject("cia1");
                    Object cia2Obj = rs.getObject("cia2");
                    Object assignmentObj = rs.getObject("assignment_quiz_lab");
                    Object eseObj = rs.getObject("ese_marks");

                    boolean absent = rs.getBoolean("ese_absent");

                    int total = rs.getInt("total_classes");
                    int attended = rs.getInt("classes_attended");

                    double attendance = total > 0
                            ? attended * 100.0 / total : 0.0;

                    System.out.println("\nCourse: " + courseCode
                            + " - " + courseTitle);
                    System.out.println("Credits: " + credits);
                    System.out.printf("Attendance: %.2f%%%n", attendance);

                    if (cia1Obj == null || cia2Obj == null
                            || assignmentObj == null
                            || (eseObj == null && !absent)) {

                        System.out.println("Grade: PENDING");
                        System.out.println("Reason: Assessment marks incomplete");
                        pendingResult = true;
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
                                    courseType,
                                    cia1, cia2, assignment, ese, absent,
                                    attendance, false);

                    System.out.println("Grade: " + result.grade);
                    System.out.println("Grade Point: " + result.gradePoint);
                    System.out.println("Status: " + result.status);

                    if ("PENDING".equals(result.grade)) {
                        pendingResult = true;
                    } else {
                        totalCredits += credits;
                        weightedGradePoints += credits * result.gradePoint;
                    }
                }
            }
        }

        System.out.println("\n========================================");

        if (!found) {
            System.out.println("No enrolled courses found.");
        } else if (pendingResult) {
            System.out.println("SGPA: PENDING");
            System.out.println(
                    "Reason: Complete all assessments and resolve pending eligibility.");
        } else if (totalCredits <= 0.0) {
            System.out.println("SGPA cannot be calculated: no valid credits.");
        } else {
            double sgpa = weightedGradePoints / totalCredits;
            double percentage = sgpa * 10.0;

            System.out.printf("Total Credits: %.2f%n", totalCredits);
            System.out.printf("SGPA: %.2f%n", sgpa);
            System.out.printf("Equivalent Percentage: %.2f%%%n", percentage);
        }

        System.out.println("========================================");
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter student roll number: ");
            String rollNo = sc.nextLine().trim();

            try {
                new SGPAReport().generate(rollNo);
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }
}
