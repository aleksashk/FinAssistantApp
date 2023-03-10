package com.aleksandrphilimonov.finAssistApp.dao;

import com.aleksandrphilimonov.finAssistApp.exception.CustomException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private final DataSource dataSource;

    public AccountDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("password");
        dataSource = new HikariDataSource(config);
    }

    public List<AccountModel> findAllByUserId(long userId) {

        List<AccountModel> accountModelList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("select * from account where user_Id = ?");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AccountModel accountModel = new AccountModel();
                accountModel.setId(rs.getLong("id"));
                accountModel.setTitle(rs.getString("title"));
                accountModel.setAmount(new BigDecimal(rs.getLong("amount")));
                accountModel.setUserId(rs.getLong("user_id"));

                accountModelList.add(accountModel);
            }

        } catch (SQLException e) {
            throw new CustomException(e);
        }

        return accountModelList;
    }

    public AccountModel findByTitleAndUserId(String title, long userId) {
        AccountModel accountModel = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("select * from account where title = ? and user_Id = ?");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                accountModel = new AccountModel();
                accountModel.setId(rs.getLong("id"));
                accountModel.setTitle(rs.getString("title"));
                accountModel.setAmount(new BigDecimal(rs.getLong("amount")));
                accountModel.setUserId(rs.getLong("user_id"));
            }

        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return accountModel;
    }

    public AccountModel create(String title, double balance, long userId) {
        AccountModel accountModel = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("insert into account (id, title, balance, user_id) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setDouble(2, balance);
            ps.setLong(3, userId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                accountModel = new AccountModel();
                accountModel.setId(rs.getLong("id"));
                accountModel.setTitle(rs.getString("title"));
                accountModel.setAmount(new BigDecimal(rs.getLong("amount")));
                accountModel.setUserId(rs.getLong("user_id"));
                return accountModel;
            } else {
                throw new CustomException("Invalid data. New ID didn't generate.");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public void delete(long id, long userId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("delete from account where id = ? and user_id = ?");
            ps.setLong(1, id);
            ps.setLong(2, userId);
            if (ps.executeUpdate() != 1) {
                throw new CustomException("Invalid data. There is no account with ID = " + id + " and user_id = " + userId + " in the database.");
            } else {
                System.out.println("Account with id = " + id + " and user_id = " + userId + " is delete.");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }
}
