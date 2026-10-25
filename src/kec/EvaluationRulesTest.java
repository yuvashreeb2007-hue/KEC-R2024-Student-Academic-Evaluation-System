
package kec;

public class EvaluationRulesTest {

    private static void test(
            String name,
            String courseType,
            double cia1,
            double cia2,
            double assignment,
            double ese,
            boolean absent,
            double attendance,
            boolean condonation) {

        EvaluationEngine.EvaluationResult result =
                EvaluationEngine.evaluate(
                        courseType,
                        cia1, cia2, assignment, ese,
                        absent, attendance, condonation);

        System.out.println("\nTest: " + name);
        System.out.println("Grade: " + result.grade);
        System.out.println("Grade Point: " + result.gradePoint);
        System.out.println("Status: " + result.status);
        System.out.printf("Total Marks: %.2f%n", result.totalMarks);
    }

    public static void main(String[] args) {

        System.out.println("===== KEC R2024 EVALUATION TESTS =====");

        test("Theory - Normal Pass",
                "THEORY", 78, 82, 90, 75,
                false, 84, false);

        test("Theory - ESE Minimum Not Met",
                "THEORY", 90, 90, 90, 40,
                false, 90, false);

        test("Attendance Below 65%",
                "THEORY", 80, 80, 80, 80,
                false, 60, false);

        test("Conditional Attendance Without Condonation",
                "THEORY", 80, 80, 80, 80,
                false, 70, false);

        test("Conditional Attendance With Condonation",
                "THEORY", 80, 80, 80, 80,
                false, 70, true);

        test("Student Absent for ESE",
                "THEORY", 80, 80, 80, 0,
                true, 90, false);

        System.out.println("\n===== TESTS COMPLETED =====");
    }
}
