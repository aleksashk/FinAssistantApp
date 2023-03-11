package com.aleksandrphilimonov.finAssistApp.converter;

import com.aleksandrphilimonov.finAssistApp.dao.AccountModel;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryModel;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryReportModel;
import com.aleksandrphilimonov.finAssistApp.dao.UserModel;
import com.aleksandrphilimonov.finAssistApp.service.AccountDTO;
import com.aleksandrphilimonov.finAssistApp.service.CategoryDTO;
import com.aleksandrphilimonov.finAssistApp.service.CategoryReportDTO;
import com.aleksandrphilimonov.finAssistApp.service.UserDTO;

public class ConverterFactory {
    private static Converter<AccountModel, AccountDTO> accountDtoConverter;

    public static Converter<AccountModel, AccountDTO> getAccountDtoConverter() {
        if (accountDtoConverter == null) {
            accountDtoConverter = new AccountModelToDtoConverter();
        }
        return accountDtoConverter;
    }

    private static Converter<CategoryModel, CategoryDTO> categoryDtoConverter;

    public static Converter<CategoryModel, CategoryDTO> getCategoryDtoConverter() {
        if (categoryDtoConverter == null) {
            categoryDtoConverter = new CategoryModelToDtoConverter();
        }
        return categoryDtoConverter;
    }

    private static Converter<CategoryReportModel, CategoryReportDTO> reportDtoConverter;

    public static Converter<CategoryReportModel, CategoryReportDTO> getCategoryReportDtoConverter() {
        if (reportDtoConverter == null) {
            reportDtoConverter = new ReportModelToDtoConverter();
        }
        return reportDtoConverter;
    }

    private static Converter<UserModel, UserDTO> userDtoConverter;

    public static Converter<UserModel, UserDTO> getUserDtoConverter() {
        if (userDtoConverter == null) {
            userDtoConverter = new UserModelToUserDtoConverter();
        }
        return userDtoConverter;
    }
}
