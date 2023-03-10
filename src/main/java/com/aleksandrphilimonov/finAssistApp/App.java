package com.aleksandrphilimonov.finAssistApp;

import com.aleksandrphilimonov.finAssistApp.service.AccountDTO;
import com.aleksandrphilimonov.finAssistApp.service.AccountService;
import com.aleksandrphilimonov.finAssistApp.service.AuthService;
import com.aleksandrphilimonov.finAssistApp.service.CategoryDTO;
import com.aleksandrphilimonov.finAssistApp.service.CategoryService;
import com.aleksandrphilimonov.finAssistApp.service.UserDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private static final AuthService authService = new AuthService();

    public static void main(String[] args) {
        UserDTO userDTO;

        String request = requestString("press 1 for authorization\npress 2 for registration");

        switch (request) {
            case "1":
                String email = requestString("Input email: ");
                String password = requestString("Input password: ");
                userDTO = authService.auth(email, password);
                if (userDTO == null) {
                    message("Invalid data.");
                } else {
                    message("User " + userDTO + " authorized.");
                    action(userDTO);
                }
                break;
            case "2":
                email = requestString("Input email: ");
                password = requestString("Input password: ");
                userDTO = authService.registration(email, password);
                if (userDTO == null) {
                    message("Invalid data.");
                } else {
                    message("User " + userDTO + " registered.");
                    action(userDTO);
                }
                break;
            default:
                message("Invalid choose. EXIT");
                break;
        }

    }

    private static String requestString(String s) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(s);
        return scanner.next();
    }

    public static Integer requestInteger(String message) {
        message(message);
        Scanner scanner = new Scanner(System.in);
        String sNumber = scanner.next();
        Integer number = null;

        try {
            number = Integer.parseInt(sNumber);
        } catch (IllegalArgumentException ignore) {
            System.out.println("Invalid value: " + sNumber + ". Try again.");
            requestInteger(message);
        }
        return number;
    }

    private static void action(UserDTO userDTO) {
        AccountService accountService = new AccountService();
        CategoryService categoryService = new CategoryService();

        String choice = requestString("press 1 to display list of accounts\n" +
                "press 2 to create an account\n" +
                "press 3 to delete an account\n" +
                "press 4 to create a category\n" +
                "press 5 to change a category\n" +
                "press 6 to delete a category");
        while (true) {
            switch (choice) {
                case "1":
                    List<AccountDTO> accountDTOList = accountService.getAllByUserId(userDTO.getId());
                    if (accountDTOList.isEmpty()) {
                        message("User email: " + userDTO.getEmail() + " doesn't have any account.");
                    } else {
                        for (AccountDTO account : accountDTOList) {
                            message(account.toString());
                        }
                    }
                    break;
                case "2":
                    String account = requestString("Input name of account: ");
                    Double balance = requestDouble("Input balance of account [X.XX]: ");

                    AccountDTO accountDTO = accountService.addAccount(account, BigDecimal.valueOf(balance), userDTO.getId());
                    if (accountDTO == null) {
                        message("Account already exists.");
                    } else {
                        message("new account: \n" + accountDTO);
                    }
                    break;
                case "3":
                    accountDTOList = accountService.getAllByUserId(userDTO.getId());
                    if (accountDTOList == null) {
                        message("User email: " + userDTO.getEmail() + " doesn't have accounts.");
                    } else {
                        for (AccountDTO item : accountDTOList) {
                            message(item.toString());
                        }
                        int id = requestInteger("Inpunt accountId for remove: ");
                        accountDTO = accountDTOList.get(id - 1);
                        accountService.deleteAccount(accountDTO.getId(), userDTO.getId());
                        message("The account id = " + id +
                                " number:" + accountDTO.getTitle() +
                                " deleted.");
                    }
                    break;
                case "4":
                    String category = requestString("Input a category name: ");
                    CategoryDTO categoryDTO = categoryService.insert(category, userDTO.getId());
                    if (categoryDTO == null) {
                        message("Category: " + category + " already exists.");
                    } else {
                        message("Category: " + category + " added.");
                    }
                    break;
                case "5":
                    List<CategoryDTO> categoryDTOList = categoryService.getAllByUserId(userDTO.getId());
                    if (categoryDTOList.isEmpty()) {
                        message("Category list is empty.");
                    } else {
                        for (CategoryDTO item : categoryDTOList) {
                            message(item.getId() + 1 +
                                    " " + item.getName() +
                                    " " + item.getUserId());
                        }
                        int id = requestInteger("Inpunt categoryId for rename: ");
                        String newNameCategory = requestString("Input new name for the category: ");
                        categoryDTO = categoryDTOList.get(id - 1);
                        categoryDTO = categoryService.update(categoryDTO.getId(), newNameCategory, userDTO.getId());
                        if (categoryDTO == null) {
                            message("Category already exists.");
                        } else {
                            message(categoryDTO + "\n Category name changed.");
                        }
                    }
                    break;
                case "6":
                    categoryDTOList = categoryService.getAllByUserId(userDTO.getId());
                    if (categoryDTOList.isEmpty()) {
                        message("Category list is empty.");
                    } else {
                        for (CategoryDTO item : categoryDTOList) {
                            message(item.getId() + 1 +
                                    " " + item.getName() +
                                    " " + item.getUserId());
                        }
                        int id = requestInteger("Inpunt categoryId for remove: ");
                        categoryDTO = categoryDTOList.get(id - 1);
                        categoryService.delete(categoryDTO.getId(), userDTO.getId());
                        message("Category " + categoryDTO.getName() + " deleted.");
                    }
                    break;
                default:
                    message("Invalid choose. EXIT");
                    return;
            }
            choice = requestString("-----------------------------------------\n" +
                    "press 1 to display list of accounts\n" +
                    "press 2 to create an account\n" +
                    "press 3 to delete an account\n" +
                    "press 4 to create a category\n" +
                    "press 5 to change a category\n" +
                    "press 6 to delete a category");
        }
    }

    public static Double requestDouble(String message) {
        message(message);

        Scanner scanner = new Scanner(System.in);
        String sNumber = scanner.next();
        Double number = null;

        try {
            number = Double.parseDouble(sNumber);
        } catch (IllegalArgumentException ignore) {
            System.out.println("Invalid value: " + sNumber + ". Try again.");
            requestInteger(message);
        }
        return number;
    }

    public static void message(String string) {
        System.out.println(string);
    }
}