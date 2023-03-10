package com.aleksandrphilimonov.finAssistApp.dao;

import com.aleksandrphilimonov.finAssistApp.exception.CustomException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {
    private final DataSource dataSource;

    public UserDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("password");

        dataSource = new HikariDataSource(config);
    }

    public UserModel findByEmailAndHash(String email, String hash) {

        UserModel userModel = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("select * from postgres.public.service_user where email = ? and password = ?");
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
        try (Connection connection = dataSource.getConnection()) {
            if (!isEmailExists(email)) {
                PreparedStatement ps = connection.prepareStatement("insert into postgres.public.service_user (email, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, email);
                ps.setString(2, hash);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    UserModel userModel = new UserModel();
                    userModel.setId(rs.getLong("id"));
                    userModel.setEmail(rs.getString("email"));
                    userModel.setPassword(rs.getString("password"));
                    return userModel;
                } else {
                    throw new CustomException("Invalid data. New ID didn't generate.");
                }
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return null;
    }

    private boolean isEmailExists(String email) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select email from postgres.public.service_user");
            while (rs.next()) {
                if (rs.getString("email").equals(email)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }
}
