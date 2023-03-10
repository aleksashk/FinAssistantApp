package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.UserModelToDTOConverter;
import com.aleksandrphilimonov.finAssistApp.dao.UserDao;

import javax.jws.soap.SOAPBinding;
import java.util.Optional;

public class AuthService {

    private final UserDao userDao;
    private final DigestService digestService;
    private final UserModelToDTOConverter userDtoConverter;

    public AuthService() {
        this.userDao = new UserDao();
        this.digestService = new Md5DigestService();
        this.userDtoConverter = new UserModelToDTOConverter();
    }

    public UserDTO auth(String email, String password) {
        String hash = digestService.hex(password);
        return Optional.ofNullable(userDao.findByEmailAndHash(email, hash)).map(userDtoConverter::convert).orElse(null);
    }

    public UserDTO registration(String email, String password) {
        String hash = digestService.hex(password);
        return Optional.ofNullable(userDao.insert(email, hash)).map(userDtoConverter::convert).orElse(null);
    }
}
