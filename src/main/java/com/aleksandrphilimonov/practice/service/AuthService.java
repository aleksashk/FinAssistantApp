package com.aleksandrphilimonov.practice.service;

import com.aleksandrphilimonov.practice.converter.UserModelToUserDtoConverter;
import com.aleksandrphilimonov.practice.dao.UserDao;
import com.aleksandrphilimonov.practice.dao.UserModel;

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
        UserModel userModel = userDao.findByEmailAndHash(email, hash);
        if (userModel == null) {
            return null;
        }
        return userDtoConverter.convert(userModel);
    }

    public UserDTO registration(String email, String password) {
        String hash = digestService.hex(password);
        UserModel userModel = userDao.insert(email, hash);
        if (userModel == null) {
            return null;
        }
        return userDtoConverter.convert(userModel);
    }
}
