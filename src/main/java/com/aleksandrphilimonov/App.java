package com.aleksandrphilimonov;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres1", "postgres", "password");

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from service_user");
        while (resultSet.next()) {
            System.out.println(resultSet.getLong("id") + " | " + resultSet.getString("email") + " | " + resultSet.getString("password"));
        }
    }
}
