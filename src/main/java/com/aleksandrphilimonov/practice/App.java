package com.aleksandrphilimonov.practice;

import com.aleksandrphilimonov.practice.service.AuthService;
import com.aleksandrphilimonov.practice.service.UserDTO;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        AuthService authService = new AuthService();

        String email = request("Введите email:");
        String password = request("Введите password:");

        UserDTO userDTO = authService.auth(email, password);

        System.out.println(userDTO);
    }

    static String request(String title) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        return scanner.next();
    }
}
