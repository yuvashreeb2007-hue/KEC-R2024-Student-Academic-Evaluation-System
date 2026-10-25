
package kec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    // Display all students
    public void viewStudents() throws SQLException {

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Student Records ---");

            while (rs.next()) {
                System.out.println(
                    "Roll No: " + rs.getString("roll_no")
                    + " | Name: " + rs.getString("student_name")
                    + " | Department: " + rs.getString("department")
                    + " | Semester: " + rs.getInt("current_semester")
                );
            }
        }
    }

    // Register a new student
    public void addStudent(
            String rollNo,
            String name,
            String department,
            int semester) throws SQLException {

        String sql =
            "INSERT INTO students " +
            "(roll_no, student_name, department, current_semester) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setInt(4, semester);

            ps.executeUpdate();
            System.out.println("Student registered successfully!");
        }
    }

    // Update student details
    public void updateStudent(
            String rollNo,
            String name,
            String department,
            int semester) throws SQLException {

        String sql =
            "UPDATE students SET student_name = ?, " +
            "department = ?, current_semester = ? " +
            "WHERE roll_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setInt(3, semester);
            ps.setString(4, rollNo);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found.");
            }
        }
    }
public void addCourse(String courseCode, String courseTitle,
                      double credits, String courseType)
        throws SQLException {

    String sql = "INSERT INTO courses " +
                 "(course_code, course_title, credits, course_type) " +
                 "VALUES (?, ?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, courseCode);
        ps.setString(2, courseTitle);
        ps.setDouble(3, credits);
        ps.setString(4, courseType);

        ps.executeUpdate();
        System.out.println("Course added successfully!");
    }
}

public void viewCourses() throws SQLException {
    String sql = "SELECT course_code, course_title, credits, course_type FROM courses";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        System.out.println("\n--- Course Records ---");

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println(
                "Course Code: " + rs.getString("course_code")
                + " | Title: " + rs.getString("course_title")
                + " | Credits: " + rs.getDouble("credits")
                + " | Type: " + rs.getString("course_type")
            );
        }

        if (!found) {
            System.out.println("No courses found.");
        }
    }
}

}

