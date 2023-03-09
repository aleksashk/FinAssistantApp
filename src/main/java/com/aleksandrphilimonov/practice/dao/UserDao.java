package com.aleksandrphilimonov.practice.dao;

import com.aleksandrphilimonov.practice.exception.CustomException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final DataSource dataSource;

    public UserDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres1");
        config.setUsername("postgres");
        config.setPassword("password");

        dataSource = new HikariDataSource(config);
    }

    public UserModel findByEmailAndHash(String email, String hash) {
        UserModel userModel = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("select * from postgres1.public.service_user where email = ? and password = ?");
            ps.setString(1, email);
            ps.setString(2, hash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userModel = new UserModel();
                userModel.setId(rs.getLong("id"));
                userModel.setEmail(rs.getString("email"));
                userModel.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return userModel;
    }

    public UserModel insert(String email, String hash) {
        return null;
    }
}
