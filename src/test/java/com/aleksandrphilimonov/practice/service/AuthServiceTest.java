package com.aleksandrphilimonov.practice.service;

import com.aleksandrphilimonov.practice.converter.UserModelToUserDtoConverter;
import com.aleksandrphilimonov.practice.dao.UserDao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    AuthService subj;

    UserDao userDao;
    DigestService digestService;
    UserModelToUserDtoConverter userDtoConverter;

    @Before
    public void setUp() throws Exception {
        userDao = mock(UserDao.class);
        digestService = mock(DigestService.class);
        userDtoConverter = mock(UserModelToUserDtoConverter.class);
        subj = new AuthService(userDao, digestService, userDtoConverter);
    }

    @Test
    public void authUserNotFound() {
        when(digestService.hex("password")).thenReturn("hex");
        when(userDao.findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex")).thenReturn(null);

        UserDTO user = subj.auth("aleksandrphilimonov@gmail.com", "password");

        assertNull(user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex");
        verifyZeroInteractions(userDtoConverter);
    }

    @Test
    public void registration() {
    }
}