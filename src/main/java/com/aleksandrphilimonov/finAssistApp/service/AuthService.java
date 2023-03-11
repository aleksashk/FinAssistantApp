package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.UserModelToUserDtoConverter;
import com.aleksandrphilimonov.finAssistApp.dao.UserDao;
import com.aleksandrphilimonov.finAssistApp.dao.UserModel;

import java.util.Optional;

public class AuthService {

    private final UserDao userDao;
    private final DigestService digestService;
    private final UserModelToUserDtoConverter userDtoConverter;

    public AuthService(UserDao userDao, DigestService digestService, UserModelToUserDtoConverter userDtoConverter) {
        this.userDao = userDao;
        this.digestService = digestService;
        this.userDtoConverter = userDtoConverter;
    }

    public UserDTO auth(String email, String password) {
        String hash = digestService.hex(password);
        return Optional.ofNullable(userDao.findByEmailAndHash(email, hash))
                .map(userDtoConverter::convert).orElse(null);

    }

    public UserDTO registration(String email, String password) {
        String hash = digestService.hex(password);
        if (userDao.findByEmailAndHash(email, hash) == null) {
            UserModel userModel = userDao.insert(email, hash);
            return userDtoConverter.convert(userModel);
        }
        return null;
    }
}
