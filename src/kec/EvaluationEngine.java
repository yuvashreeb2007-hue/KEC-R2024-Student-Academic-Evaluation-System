
package kec;

public class EvaluationEngine {

    public static class EvaluationResult {
        public double ciaScore;
        public double weightedCIA;
        public double weightedESE;
        public double totalMarks;
        public String grade;
        public int gradePoint;
        public String status;

        public EvaluationResult(double ciaScore, double weightedCIA,
                double weightedESE, double totalMarks,
                String grade, int gradePoint, String status) {
            this.ciaScore = ciaScore;
            this.weightedCIA = weightedCIA;
            this.weightedESE = weightedESE;
            this.totalMarks = totalMarks;
            this.grade = grade;
            this.gradePoint = gradePoint;
            this.status = status;
        }
    }

    public static EvaluationResult evaluate(
            String courseType,
            double cia1,
            double cia2,
            double assignment,
            double ese,
            boolean eseAbsent,
            double attendancePercentage,
            boolean medicalCondonationApproved) {

        if (cia1 < 0 || cia1 > 100
                || cia2 < 0 || cia2 > 100
                || assignment < 0 || assignment > 100
                || ese < 0 || ese > 100
                || attendancePercentage < 0
                || attendancePercentage > 100) {
            throw new IllegalArgumentException(
                    "Marks and attendance must be between 0 and 100.");
        }

        String type = courseType.trim().toUpperCase();

        double ciaWeight;
        double eseWeight;
        double eseMinimum;

        switch (type) {
            case "THEORY":
                ciaWeight = 0.40;
                eseWeight = 0.60;
                eseMinimum = 27.0;
                break;

            case "PRACTICAL":
                ciaWeight = 0.60;
                eseWeight = 0.40;
                eseMinimum = 20.0;
                break;

            case "EMBEDDED":
                ciaWeight = 0.50;
                eseWeight = 0.50;
                eseMinimum = 22.5;
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid course type: " + courseType);
        }

        double cia = (cia1 + cia2 + assignment) / 3.0;
        double weightedCIA = cia * ciaWeight;
        double weightedESE = ese * eseWeight;
        double total = weightedCIA + weightedESE;

        // Attendance below 65% results in SA.
        if (attendancePercentage < 65.0) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "SA", 0, "Shortage of Attendance");
        }

        // Attendance from 65% to below 75% requires approval.
        if (attendancePercentage < 75.0
                && !medicalCondonationApproved) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "PENDING", 0,
                    "Medical condonation approval required");
        }

        // AB takes precedence over ordinary marks-based grading.
        if (eseAbsent) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "AB", 0, "Absent in ESE");
        }

        // ESE minimum is checked using its weighted contribution.
        if (weightedESE < eseMinimum || total < 50.0) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "RA", 0, "Re-Appearance / Fail");
        }

        if (total >= 91.0) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "O", 10, "PASS");
        } else if (total >= 81.0) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "A+", 9, "PASS");
        } else if (total >= 71.0) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "A", 8, "PASS");
        } else if (total >= 61.0) {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "B+", 7, "PASS");
        } else {
            return new EvaluationResult(
                    cia, weightedCIA, weightedESE, total,
                    "B", 6, "PASS");
        }
    }
}
