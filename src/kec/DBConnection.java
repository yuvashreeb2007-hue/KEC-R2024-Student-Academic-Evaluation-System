
package kec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mariadb://localhost:3306/kec_academic";

    private static final String USER = "kec_app";

    private static final String PASSWORD =
        "ChangeThisPassword!";

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
            URL, USER, PASSWORD
        );
    }
}
