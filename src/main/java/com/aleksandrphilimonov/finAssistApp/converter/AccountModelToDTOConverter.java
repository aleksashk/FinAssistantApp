package com.aleksandrphilimonov.finAssistApp.converter;

import com.aleksandrphilimonov.finAssistApp.dao.AccountModel;
import com.aleksandrphilimonov.finAssistApp.service.AccountDTO;

public class AccountModelToDTOConverter implements Converter<AccountModel, AccountDTO> {
    @Override
    public AccountDTO convert(AccountModel source) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(source.getId());
        accountDTO.setTitle(source.getTitle());
        accountDTO.setAmount(source.getAmount());
        accountDTO.setUserId(source.getUserId());
        return accountDTO;
    }
}
