package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.AccountModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.dao.AccountDao;
import com.aleksandrphilimonov.finAssistApp.dao.AccountModel;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AccountService {
    private final AccountDao accountDao;
    private final AccountModelToDtoConverter accountModelToDtoConverter;

    public AccountService(AccountDao accountDao, AccountModelToDtoConverter accountModelToDtoConverter) {
        this.accountDao = accountDao;
        this.accountModelToDtoConverter = accountModelToDtoConverter;
    }

    public List<AccountDTO> getAllByUserId(long userId) {
        return accountDao.findAllByUserId(userId)
                .stream()
                .map(accountModelToDtoConverter::convert)
                .collect(toList());
    }

    public AccountDTO addAccount(String title, BigDecimal balance, long userId) {
        AccountModel accountModel;
        if (accountDao.findByTitleAndUserId(title, userId) == null) {
            accountModel = accountDao.create(title, balance.doubleValue(), userId);
            return accountModelToDtoConverter.convert(accountModel);
        }
        return null;
    }

    public void deleteAccount(long id, long userId) {
        accountDao.delete(id, userId);
    }
}
