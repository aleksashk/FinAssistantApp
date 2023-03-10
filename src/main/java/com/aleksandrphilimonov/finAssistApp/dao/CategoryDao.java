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

    public List<CategoryModel> getAllByUserId(long userId) {
        List<CategoryModel> categoryModelList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("select * from category where user_id = ?");
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setId(rs.getLong("id"));
                categoryModel.setName(rs.getString("name"));
                categoryModel.setUserId(rs.getLong("user_id"));

                categoryModelList.add(categoryModel);
            }
            return categoryModelList;
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public CategoryModel findByNameAndUserId(String name, long userId) {
        CategoryModel categoryModel = null;

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("select * from category where name = ? and user_id = ?");
            ps.setString(1, name);
            ps.setLong(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categoryModel = new CategoryModel();
                categoryModel.setId(rs.getLong("id"));
                categoryModel.setName(rs.getString("name"));
                categoryModel.setUserId(rs.getLong("user_id"));
            }
            return categoryModel;
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public CategoryModel create(String name, long userId) {
        CategoryModel categoryModel = null;

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("insert into category (name, user_id) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setLong(2, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                categoryModel = new CategoryModel();
                categoryModel.setId(rs.getLong("id"));
                categoryModel.setName(rs.getString("name"));
                categoryModel.setUserId(rs.getLong("user_id"));
                return categoryModel;
            } else {
                throw new CustomException("Invalid data. Category didn't create.");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public CategoryModel update(long id, String newName, long userId) {
        CategoryModel categoryModel = null;

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("update category set name = ? where id = ? and  user_id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newName);
            ps.setLong(2, id);
            ps.setLong(3, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                categoryModel = new CategoryModel();
                categoryModel.setId(rs.getLong("id"));
                categoryModel.setName(rs.getString("name"));
                categoryModel.setUserId(rs.getLong("user_id"));
                return categoryModel;
            } else {
                throw new CustomException("Invalid data. Category didn't update.");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public void delete(long id, long userId) {
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("delete from category where id = ? and  user_id = ?");
            ps.setLong(1, id);
            ps.setLong(2, userId);
            int i = ps.executeUpdate();
            if (i != 1) {
                throw new CustomException("Invalid data. Category didn't delete.");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }
}
