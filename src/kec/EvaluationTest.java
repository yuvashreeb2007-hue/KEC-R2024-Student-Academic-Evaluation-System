
package kec;

public class EvaluationTest {

    public static void main(String[] args) {

        EvaluationEngine.EvaluationResult result =
                EvaluationEngine.evaluate(
                        "THEORY",
                        78,     // CIA 1
                        82,     // CIA 2
                        90,     // Assignment / Quiz / Lab
                        75,     // ESE
                        false,  // ESE absent
                        84.0,   // Attendance percentage
                        false   // Medical condonation approved
                );

        System.out.println("\n--- Evaluation Result ---");
        System.out.printf("CIA Average: %.2f%n", result.ciaScore);
        System.out.printf("Weighted CIA: %.2f%n", result.weightedCIA);
        System.out.printf("Weighted ESE: %.2f%n", result.weightedESE);
        System.out.printf("Total Marks: %.2f%n", result.totalMarks);
        System.out.println("Grade: " + result.grade);
        System.out.println("Grade Point: " + result.gradePoint);
        System.out.println("Status: " + result.status);
    }
}
