package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.AccountModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.dao.AccountDao;
import com.aleksandrphilimonov.finAssistApp.dao.AccountModel;
import com.aleksandrphilimonov.finAssistApp.exception.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountDao accountDao;

    @Mock
    AccountModelToDtoConverter accountModelToDtoConverter;

    @Test
    public void get_all_by_user_id_not_exists() {
        when(accountDao.findAllByUserId(1)).thenReturn(Collections.emptyList());
        List<AccountDTO> list = accountService.getAllByUserId(1);

        assertTrue(list.isEmpty());

        verify(accountDao, times(1)).findAllByUserId(1);
        verifyZeroInteractions(accountModelToDtoConverter);
    }

    @Test
    public void get_all_by_user_id_exists() {

        List<AccountModel> list = Arrays.asList(new AccountModel(), new AccountModel());

        when(accountDao.findAllByUserId(1)).thenReturn(list);

        when(accountModelToDtoConverter.convert(new AccountModel())).thenReturn(new AccountDTO());

        List<AccountDTO> listDTO = Arrays.asList(new AccountDTO(), new AccountDTO());

        List<AccountDTO> accountDTOList = accountService.getAllByUserId(1);

        assertEquals(listDTO, accountDTOList);

        verify(accountDao, times(1)).findAllByUserId(1);
        verify(accountModelToDtoConverter, times(list.size())).convert(new AccountModel());
    }

    @Test
    public void add_account_successful() {
        when(accountDao.findByTitleAndUserId("alex@gmail.com", 1)).thenReturn(null);
        AccountModel accountModel = new AccountModel();
        accountModel.setId(2);
        accountModel.setUserId(1);
        accountModel.setTitle("alex@gmail.com");
        accountModel.setBalance(new BigDecimal(1));
        when(accountDao.create("alex@gmail.com", 1, 1)).thenReturn(accountModel);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(2);
        accountDTO.setUserId(1);
        accountDTO.setTitle("alex@gmail.com");
        accountDTO.setBalance(new BigDecimal(1));
        when(accountModelToDtoConverter.convert(accountModel)).thenReturn(accountDTO);

        AccountDTO account = accountService.addAccount("alex@gmail.com", new BigDecimal(1), 1);

        assertNotNull(account);
        assertEquals(accountDTO, account);

        verify(accountDao, times(1)).create("alex@gmail.com", 1, 1);
        verify(accountDao, times(1)).findByTitleAndUserId("alex@gmail.com", 1);
        verify(accountModelToDtoConverter, times(1)).convert(accountModel);
    }

    @Test
    public void add_account_failed() {
        AccountModel accountModel = new AccountModel();
        accountModel.setId(2);
        accountModel.setUserId(2);
        accountModel.setTitle("jack@gamil.com");
        accountModel.setBalance(new BigDecimal(22));

        when(accountDao.findByTitleAndUserId("jack@gmail.com", 2)).thenReturn(accountModel);

        AccountDTO accountDTO = accountService.addAccount("jack@gmail.com", new BigDecimal(22), 2);
        assertNull(accountDTO);

        verify(accountDao, times(1)).findByTitleAndUserId("jack@gmail.com", 2);
        verify(accountDao, times(0)).create("jack@gmail.com", 22, 2);
        verifyZeroInteractions(accountModelToDtoConverter);
    }

    @Test
    public void delete_account_successful() {
        accountService.deleteAccount(1, 1);
        verify(accountDao, times(1)).delete(1, 1);
    }

    @Test(expected = CustomException.class)
    public void deleteAccount_failed() {
        doThrow(new CustomException("Invalid data. Account couldn't delete.")).when(accountDao).delete(1, 1);

        accountService.deleteAccount(1, 1);
        verify(accountDao, times(1)).delete(1, 1);
    }
}