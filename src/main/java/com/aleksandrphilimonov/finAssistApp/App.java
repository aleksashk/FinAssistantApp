package com.aleksandrphilimonov.finAssistApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static String MESSAGE;
    public static Connection CONNECTION;
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String LOGIN = "postgres";
    public static final String PASSWORD = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MESSAGE = "Выберите действие: \n" +
                "/t 1 - регистрация нового пользователя;" +
                "/t 2 - авторизация пользователя;" +
                "/t 3 - выход из программы;";

        System.out.println(MESSAGE);

        String chose = scanner.nextLine();
        try {
            CONNECTION = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            while (true) {
                switch (chose) {
                    case "1":
                        registration();
                        break;
                    case "2":
                        authorisation();
                        break;
                    case "3":
                        System.out.println("Вы вышли из системы.");
                        return;
                    default:
                        System.out.println("Некорректный ввод. Попробуйте ещё раз");
                }
                MESSAGE = "Выберите действие:\n" +
                        "\tНажмите 1 для регистрации нового пользователя;\n" +
                        "\tНажмите 2 для авторизации пользователя;\n" +
                        "\tНажмите 3 выхода из программы;";
                System.out.println(MESSAGE);
                chose = scanner.nextLine();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                CONNECTION.close();
            } catch (SQLException ignore) {
            }
        }
    }

    private static void registration() {
        String userData = getUser();
        String email = userData.split(" ")[0];
        String password = userData.split(" ")[1];

        try {
            PreparedStatement preparedStatementExist = CONNECTION.prepareStatement("select * from service_user where email = ?");
            preparedStatementExist.setString(1, email);

            PreparedStatement preparedStatementNotExist = CONNECTION.prepareStatement("insert into service_user (email, password) values (?, ?)");

            ResultSet resultSet = preparedStatementExist.executeQuery();
            if (resultSet.next()) {
                MESSAGE = "Данный пользователь уже зарегистрирован.";
            } else {
                preparedStatementNotExist.setString(1, email);
                preparedStatementNotExist.setString(2, password);
                if (preparedStatementNotExist.executeUpdate() != 0) {
                    MESSAGE = "Регистрация пользователя прошла успешно.";
                } else {
                    MESSAGE = "Регистрация не завершена, некорректные данные.";
                }
            }
            System.out.println(MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getUser() {
        StringBuilder userData = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        MESSAGE = "Введите email пользователя: ";
        System.out.println(MESSAGE);
        userData.append(scanner.nextLine())
                .append(" ");
        MESSAGE = "Введите пароль (не менее 5 символов): ";
        System.out.println(MESSAGE);
        userData.append(scanner.nextLine());

        try {
            userChecker(userData);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return userData.toString();
    }

    private static void userChecker(StringBuilder userData) throws IllegalArgumentException {
        if (userData.toString().split(" ")[0].length() < 5 ||
                userData.toString().split(" ")[1].length() < 5) {
            throw new IllegalArgumentException("Некорректные данные нового пользователя!");
        }
    }

    private static void authorisation() throws SQLException {

        String userData = getUser();
        String email = userData.split(" ")[0];
        String password = userData.split(" ")[1];

        PreparedStatement preparedStatement = CONNECTION.prepareStatement("select * from service_user where email = ? and password = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            MESSAGE = "Вы вошли в систему как пользователь с email: " + email;
        } else {
            MESSAGE = "Неверные данные";
            return;
        }
        System.out.println(MESSAGE);

        MESSAGE = "Выберите действие:\n" +
                "\tНажмите 1 для просмотра списка счетов пользователя;\n" +
                "\tНажмите 2 для создания нового счета;\n" +
                "\tНажмите 3 для удаления счета;\n" +
                "\tНажмите 0 для выхода из системы;";
        System.out.println(MESSAGE);

        Scanner scanner = new Scanner(System.in);
        String chose = scanner.nextLine();
        while (true) {
            switch (chose) {
                case "1":
                    showAccounts(email);
                    break;
                case "2":
                    createAccount(email);
                    break;
                case "3":
                    deleteAccount(email);
                    break;
                case "0":
                    System.out.println("Вы вышли из системы.");
                    return;
                default:
                    System.out.println("Некорректный ввод. Попробуйте ещё раз.");
            }
            System.out.println(MESSAGE);
            chose = scanner.nextLine();
        }
    }

    private static void deleteAccount(String email) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        MESSAGE = "Введите номер удаляемого счета: ";
        System.out.println(MESSAGE);
        String account = scanner.next();
        PreparedStatement preparedStatement = CONNECTION.prepareStatement("delete from account where user_id = (" +
                "select su.id " +
                "from service_user as su " +
                "where su.email = ?)" +
                " and title = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, account);

        if (preparedStatement.executeUpdate() != 0) {
            MESSAGE = "Счёт №" + account + " успешно удалён.";
        } else {
            MESSAGE = "Счёт №" + account + " у пользователя с email " + email + " не найден.";
        }
        System.out.println(MESSAGE);
        preparedStatement.close();
    }

    private static void createAccount(String email) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        MESSAGE = "Введите номер нового счета: ";
        System.out.println(MESSAGE);
        String account = scanner.next();
        PreparedStatement preparedStatement = CONNECTION.prepareStatement(
                "select a.title " +
                        "from account as a " +
                        "where a.title = ? and user_id = (" +
                        "select su.id " +
                        "from service_user as su " +
                        "where su.email = ?)");
        preparedStatement.setString(1, account);
        preparedStatement.setString(2, email);

        PreparedStatement preparedStatementNewAccount = CONNECTION.prepareStatement(
                "insert into account values (?, 0.00, (select su.id from service_user as su where su.email = ?))"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            MESSAGE = "Счет с номером: " + account +
                    " у пользователя с email: " +
                    email +
                    " уже существует";
        } else {
            preparedStatementNewAccount.setString(1, account);
            preparedStatementNewAccount.setString(2, email);

            if (preparedStatementNewAccount.executeUpdate() != 0) {
                MESSAGE = "Пользователю с email: " + email + " добавлен счет";
            } else {
                MESSAGE = "Ошибка регистрации нового счета для пользователя с email: " + email;
            }
            System.out.println(MESSAGE);
            preparedStatement.close();
            preparedStatementNewAccount.close();
        }
    }

    private static void showAccounts(String email) throws SQLException {
        PreparedStatement preparedStatement = CONNECTION.prepareStatement(
                "select su.email, a.title\n" +
                        "from service_user as su\n" +
                        "join account a on su.id = a.user_id\n" +
                        "where email = ?" +
                        "order by email"
        );
        preparedStatement.setString(1, email);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("У пользователя нету открытых счетов.");
        } else {
            do {
                String account = resultSet.getString(2);
                System.out.println(account);
            } while (resultSet.next());
        }
        preparedStatement.close();
    }
}