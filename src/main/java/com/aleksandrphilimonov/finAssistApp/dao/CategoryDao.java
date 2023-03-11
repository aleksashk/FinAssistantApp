package com.aleksandrphilimonov.finAssistApp.dao;

import com.aleksandrphilimonov.finAssistApp.exception.CustomException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private final DataSource dataSource;

    public CategoryDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("password");

        dataSource = new HikariDataSource(config);
    }

    public CategoryModel findByNameAndUserId(String name, long userId) {
        CategoryModel categoryModel = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from category " +
                            "where name = ? and user_id = ?"
            );
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                categoryModel = new CategoryModel();
                categoryModel.setName(resultSet.getString("name"));
                categoryModel.setUserId(resultSet.getLong("user_id"));
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return categoryModel;
    }

    public CategoryModel create(String name, long userId) {
        CategoryModel categoryModel;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into category(name, user_id) " +
                            "values (?, ?)", Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, userId);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                categoryModel = new CategoryModel();
                categoryModel.setName(resultSet.getString(2));
                categoryModel.setUserId(resultSet.getLong(3));
                return categoryModel;
            } else {
                throw new CustomException("Invalid data. Category didn't create");
            }

        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public CategoryModel update(long id, String newName, long userId) {
        CategoryModel categoryModel;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "update category set name = ? " +
                             "where id = ? and user_id = ?")) {

            preparedStatement.setString(1, newName);
            preparedStatement.setLong(2, id);
            preparedStatement.setLong(3, userId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                categoryModel = new CategoryModel();
                categoryModel.setId(resultSet.getLong("id"));
                categoryModel.setName(resultSet.getString("name"));
                categoryModel.setUserId(resultSet.getLong("user_id"));
            } else {
                throw new CustomException("Invalid data. Category didn't update");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }

        return categoryModel;
    }

    public void delete(long id, long userId) {
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from category " +
                            "where id = ? and user_id = ?"
            );
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);

            if (preparedStatement.executeUpdate() != 1) {
                throw new CustomException("Invalid request. Category didn't delete.");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public List<CategoryModel> getAllByUserId(long userId) {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from category " +
                            "where user_id = ?"
            );
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryModel categoryModel = new CategoryModel();

                categoryModel.setId(resultSet.getLong(1));
                categoryModel.setName(resultSet.getString(2));
                categoryModel.setUserId(resultSet.getLong(3));

                categoryModelList.add(categoryModel);
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return categoryModelList;
    }

    public List<CategoryReportModel> getReportMoneyOut(long userId, LocalDate fromDate, LocalDate toDate) {
        List<CategoryReportModel> listReportModelMoney = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(
                    "select sum(t.amount), c.name as name " +
                            "from transaction t " +
                            "left join account a on a.id = t.to_account_id " +
                            "left join category_transaction ct on t.id = ct.transaction_id " +
                            "left join category c on c.id = ct.category_id " +
                            "where a.user_id = ? " +
                            "and t.transaction_date between ? and ? " +
                            "group by c.name"
            );
            ps.setLong(1, userId);
            ps.setDate(2, Date.valueOf(fromDate));
            ps.setDate(3, Date.valueOf(toDate));

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                CategoryReportModel categoryReportModel = new CategoryReportModel();
                categoryReportModel.setTitle(resultSet.getString(1));
                categoryReportModel.setAmount(BigDecimal.valueOf(resultSet.getDouble(2)));
                listReportModelMoney.add(categoryReportModel);
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return listReportModelMoney;
    }

    public List<CategoryReportModel> getReportMoneyIn(long userId, LocalDate fromDate, LocalDate toDate) {
        List<CategoryReportModel> listReportModelMoney = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(
                    "select sum(t.amount), c.name as name " +
                            "from transaction t " +
                            "left join account a on a.id = t.from_account_id " +
                            "left join category_transaction ct on t.id = ct.transaction_id " +
                            "left join category c on c.id = ct.category_id " +
                            "where a.user_id = ? " +
                            "and t.transaction_date between ? and ? " +
                            "group by c.name"
            );
            ps.setLong(1, userId);
            ps.setDate(2, Date.valueOf(fromDate));
            ps.setDate(3, Date.valueOf(toDate));

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                CategoryReportModel categoryReportModel = new CategoryReportModel();
                categoryReportModel.setTitle(resultSet.getString(1));
                categoryReportModel.setAmount(BigDecimal.valueOf(resultSet.getDouble(2)));
                listReportModelMoney.add(categoryReportModel);
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return listReportModelMoney;
    }
}
