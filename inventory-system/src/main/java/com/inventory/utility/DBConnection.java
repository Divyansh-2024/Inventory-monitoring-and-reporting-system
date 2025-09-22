package com.inventory.utility;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dbstore";
    private static final String USER = "testuser";      
    private static final String PASSWORD = "Password@123"; 

    // Static method to get connection
    public static Connection getConnection() throws SQLException {     
            return DriverManager.getConnection(URL, USER, PASSWORD);        
    }
}
