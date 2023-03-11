package com.aleksandrphilimonov.practice.service;

import com.aleksandrphilimonov.practice.converter.UserModelToUserDtoConverter;
import com.aleksandrphilimonov.practice.dao.UserDao;
import com.aleksandrphilimonov.practice.dao.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {
    @InjectMocks
    AuthService subj;

    @Mock UserDao userDao;

    @Mock DigestService digestService;

    @Mock UserModelToUserDtoConverter userDtoConverter;

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
    public void authUserFound() {
        when(digestService.hex("password")).thenReturn("hex");
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("aleksandrphilimonov@gmail.com");
        userModel.setPassword("hex");
        when(userDao.findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex")).thenReturn(userModel);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("aleksandrphilimonov@gmail.com");
        when(userDtoConverter.convert(userModel)).thenReturn(userDTO);

        UserDTO user = subj.auth("aleksandrphilimonov@gmail.com", "password");

        assertNotNull(user);
        assertEquals(userDTO, user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex");
        verify(userDtoConverter, times(1)).convert(userModel);
        verifyZeroInteractions(userDtoConverter);
    }

    @Test
    public void registration() {
    }
}