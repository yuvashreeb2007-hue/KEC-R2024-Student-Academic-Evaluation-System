
package kec;

import java.sql.SQLException;

public class AttendanceTest {
    public static void main(String[] args) {
        AcademicDAO dao = new AcademicDAO();

        try {
            dao.viewAttendance("25ITR182");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
