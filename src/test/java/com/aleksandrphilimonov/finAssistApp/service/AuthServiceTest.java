package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.UserModelToUserDtoConverter;
import com.aleksandrphilimonov.finAssistApp.dao.UserDao;
import com.aleksandrphilimonov.finAssistApp.dao.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService subj;

    @Mock
    UserDao userDao;

    @Mock
    DigestService digestService;

    @Mock
    UserModelToUserDtoConverter userDtoConverter;

    @Test
    public void auth_user_not_found() {
        when(digestService.hex("password"))
                .thenReturn("hex");

        when(userDao.findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex"))
                .thenReturn(null);
        UserDTO user = subj.auth("aleksandrphilimonov@gmail.com", "password");
        assertNull(user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1))
                .findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex");
        verifyZeroInteractions(userDtoConverter);
    }

    @Test
    public void auth_user_found() {
        when(digestService.hex("password"))
                .thenReturn("hex");

        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("aleksandrphilimonov@gmail.com");
        userModel.setPassword("password");
        when(userDao.findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex"))
                .thenReturn(userModel);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("aleksandrphilimonov@gmail.com");
        when(userDtoConverter.convert(userModel)).thenReturn(userDTO);

        UserDTO user = subj.auth("aleksandrphilimonov@gmail.com", "password");
        assertNotNull(user);
        assertEquals(userDTO, user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1))
                .findByEmailAndHash("aleksandrphilimonov@gmail.com", "hex");
        verify(userDtoConverter, times(1))
                .convert(userModel);
    }

    @Test
    public void registration_user_in_db() {
        when(digestService.hex("password")).thenReturn("hex");

        when(userDao.findByEmailAndHash("alex@gmail.com", "hex")).thenReturn(new UserModel());

        UserDTO user = subj.registration("alex@gmail.com", "password");

        assertNull(user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("alex@gmail.com", "hex");
        verify(userDao, times(0)).insert("alex@gmail.com", "hex");
        verifyZeroInteractions(userDtoConverter);
    }

    @Test
    public void registration_user_not_in_db() {
        when(digestService.hex("password")).thenReturn("hex");

        when(userDao.findByEmailAndHash("alex@gmail.com", "hex")).thenReturn(null);

        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("alex@gmail.com");
        userModel.setPassword("hex");
        when(userDao.insert("alex@gmail.com", "hex")).thenReturn(userModel);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("alex.@gmail.com");
        when(userDtoConverter.convert(userModel)).thenReturn(userDTO);

        UserDTO user = subj.registration("alex@gmail.com", "password");
        assertNotNull(user);
        assertEquals(userDTO, user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("alex@gmail.com", "hex");
        verify(userDao, times(1)).insert("alex@gmail.com", "hex");
        verify(userDtoConverter, times(1)).convert(userModel);
    }

}