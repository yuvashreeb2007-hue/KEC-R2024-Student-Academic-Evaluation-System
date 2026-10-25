
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassPerformanceReport {

    public void generate() throws SQLException {

        String sql = """
            SELECT
                s.department,
                s.roll_no,
                c.credits,
                e.total_classes,
                e.classes_attended,
                a.cia1,
                a.cia2,
                a.assignment_quiz_lab,
                a.ese_marks,
                a.ese_absent,
                c.course_type,
                e.medical_condonation
            FROM students s
            JOIN enrollments e ON s.roll_no = e.roll_no
            JOIN courses c ON e.course_code = c.course_code
            LEFT JOIN assessment_marks a
                ON e.enrollment_id = a.enrollment_id
            ORDER BY s.department, s.roll_no, c.course_code
            """;

        class DepartmentSummary {
            int pass = 0;
            int fail = 0;
            double credits = 0.0;
            double weightedPoints = 0.0;
            boolean pending = false;
        }

        java.util.Map<String, DepartmentSummary> summaries =
                new java.util.LinkedHashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String department = rs.getString("department");

                DepartmentSummary summary =
                        summaries.computeIfAbsent(
                                department, key -> new DepartmentSummary());

                Object cia1Obj = rs.getObject("cia1");
                Object cia2Obj = rs.getObject("cia2");
                Object assignmentObj = rs.getObject("assignment_quiz_lab");
                Object eseObj = rs.getObject("ese_marks");

                boolean absent = rs.getBoolean("ese_absent");

                if (cia1Obj == null || cia2Obj == null
                        || assignmentObj == null
                        || (eseObj == null && !absent)) {
                    summary.pending = true;
                    continue;
                }

                int total = rs.getInt("total_classes");
                int attended = rs.getInt("classes_attended");

                double attendance = total > 0
                        ? attended * 100.0 / total : 0.0;

                double cia1 = ((Number) cia1Obj).doubleValue();
                double cia2 = ((Number) cia2Obj).doubleValue();
                double assignment =
                        ((Number) assignmentObj).doubleValue();
                double ese = eseObj == null ? 0.0
                        : ((Number) eseObj).doubleValue();

                boolean condonationApproved =
                        rs.getBoolean("medical_condonation");

                EvaluationEngine.EvaluationResult result =
                        EvaluationEngine.evaluate(
                                rs.getString("course_type"),
                                cia1, cia2, assignment, ese, absent,
                                attendance, condonationApproved);

                if ("PENDING".equals(result.grade)) {
                    summary.pending = true;
                    continue;
                }

                if ("SA".equals(result.grade)
                        || "AB".equals(result.grade)
                        || "RA".equals(result.grade)) {
                    summary.fail++;
                } else {
                    summary.pass++;
                }

                double credits = rs.getDouble("credits");
                summary.credits += credits;
                summary.weightedPoints +=
                        credits * result.gradePoint;
            }
        }

        System.out.println("\n==============================================");
        System.out.println("       KEC CLASS PERFORMANCE SUMMARY");
        System.out.println("==============================================");

        if (summaries.isEmpty()) {
            System.out.println("No enrolled student records found.");
        }

        for (java.util.Map.Entry<String, DepartmentSummary> entry
                : summaries.entrySet()) {

            DepartmentSummary summary = entry.getValue();

            System.out.println("\nDepartment: " + entry.getKey());
            System.out.println("Course Results Counted as Pass: "
                    + summary.pass);
            System.out.println("Course Results Counted as Fail: "
                    + summary.fail);

            if (summary.pending) {
                System.out.println(
                        "Average Class SGPA: PENDING (incomplete results)");
            } else if (summary.credits > 0.0) {
                double averageSGPA =
                        summary.weightedPoints / summary.credits;
                System.out.printf("Average Class SGPA: %.2f%n",
                        averageSGPA);
            } else {
                System.out.println("Average Class SGPA: Not available");
            }

            System.out.println("----------------------------------------------");
        }

        System.out.println("Note: Pass/fail counts are course results, "
                + "not unique student counts.");
        System.out.println("==============================================");
    }

    public static void main(String[] args) {
        try {
            new ClassPerformanceReport().generate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
