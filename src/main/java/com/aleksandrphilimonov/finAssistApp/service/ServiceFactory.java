package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.AccountModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.converter.CategoryModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.converter.ReportModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.dao.AccountDao;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryDao;
import com.aleksandrphilimonov.finAssistApp.dao.UserDao;
import com.aleksandrphilimonov.finAssistApp.converter.UserModelToUserDtoConverter;

public class ServiceFactory {
    private static AuthService authService;
    private static CategoryService categoryService;
    private static AccountService accountService;

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(
                    new UserDao(),
                    new Md5DigestService(),
                    new UserModelToUserDtoConverter()
            );
        }
        return authService;
    }

    public static CategoryService getCategoryService() {
        if (categoryService == null) {
            categoryService = new CategoryService(
                    new CategoryDao(),
                    new CategoryModelToDtoConverter(),
                    new ReportModelToDtoConverter()
            );
        }
        return categoryService;
    }

    public static AccountService getAccountService() {
        if (accountService == null) {
            accountService = new AccountService(
                    new AccountDao(),
                    new AccountModelToDtoConverter()
            );
        }
        return accountService;
    }
}
