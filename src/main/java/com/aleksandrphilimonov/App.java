package com.aleksandrphilimonov;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.commons.codec.digest.DigestUtils.*;

public class App {
    public static void main(String[] args) {
        String email = "aleksandrphilimonov@gmail.com";
        String password = "password";
        String passwordHex = md5Hex(password);

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres1", "postgres", "password")) {

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from service_user");
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id") + ", " + resultSet.getString("email") + ", " + resultSet.getString("password"));
            }

            statement.close();

            System.out.println();

            PreparedStatement preparedStatement = con.prepareStatement("select * from service_user where email = ? and password = ?");

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, passwordHex);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Hello " + rs.getString("email") + "!");
            } else {
                System.out.println("Access denied");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
