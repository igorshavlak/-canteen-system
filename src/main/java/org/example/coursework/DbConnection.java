package org.example.coursework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static String url = "jdbc:postgresql://localhost:5432/postgres";
    private static String password = "postgresql";
    private static String user = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

}

