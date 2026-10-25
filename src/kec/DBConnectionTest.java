
package kec;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {

        try (Connection con =
                 DBConnection.getConnection()) {

            System.out.println(
                "Database connected successfully!"
            );

            System.out.println(
                "Database: " +
                con.getCatalog()
            );

        } catch (SQLException e) {
            System.out.println(
                "Database connection failed!"
            );

            System.out.println(e.getMessage());
        }
    }
}
