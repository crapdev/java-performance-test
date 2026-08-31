
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cohorte5
 */
public class Connection {
    private static final String URL = "jdbc:postgresql://localhost:5432/taller_express";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Zxc.123*";

    private Connection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}



