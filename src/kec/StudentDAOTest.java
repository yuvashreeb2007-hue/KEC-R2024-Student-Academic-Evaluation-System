
package kec;

import java.sql.SQLException;

public class StudentDAOTest {

    public static void main(String[] args) {

        StudentDAO dao = new StudentDAO();

        try {
            dao.addStudent(
                "24ITR001",
                "Arun Kumar",
                "IT",
                3
            );

            dao.addCourse(
                "24ITR31",
                "Java Programming",
                4.0,
                "THEORY"
            );

        } catch (SQLException e) {
            System.out.println(
                "Operation failed: " + e.getMessage()
            );
        }
    }
}
