package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.AccountModelToDTOConverter;
import com.aleksandrphilimonov.finAssistApp.dao.AccountDao;
import com.aleksandrphilimonov.finAssistApp.dao.AccountModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class AccountService {
    private final AccountDao accountDao;
    private final AccountModelToDTOConverter accountModelToDTOConverter;

    public AccountService() {
        this.accountDao = new AccountDao();
        this.accountModelToDTOConverter = new AccountModelToDTOConverter();
    }

    public List<AccountDTO> getAllByUserId(long userId) {
        return accountDao.findAllByUserId(userId).stream().map(accountModelToDTOConverter::convert).collect(Collectors.toList());
    }

    public AccountDTO addAccount(String title, BigDecimal balance, long userId) {
        AccountModel accountModel;
        if (accountDao.findByTitleAndUserId(title, userId) == null) {
            accountModel = accountDao.create(title, balance.doubleValue(), userId);
            return accountModelToDTOConverter.convert(accountModel);
        }
        return null;
    }

    public void deleteAccount(long id, long userId) {
        accountDao.delete(id, userId);
    }
}
