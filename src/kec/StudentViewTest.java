
package kec;

import java.sql.SQLException;

public class StudentViewTest {

    public static void main(String[] args) {

        StudentDAO dao = new StudentDAO();

        try {
            dao.viewStudents();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
