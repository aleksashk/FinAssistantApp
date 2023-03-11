package com.aleksandrphilimonov.practice.service;

import com.aleksandrphilimonov.practice.converter.UserModelToUserDtoConverter;
import com.aleksandrphilimonov.practice.dao.UserDao;

public class ServiceFactory {
    private static AuthService authService;

    public static com.aleksandrphilimonov.practice.service.AuthService getAuthService(){
        if(authService == null){
            authService = new AuthService(
                    new UserDao(),
                    new Md5DigestService(),
                    new UserModelToUserDtoConverter()
            );
        }
        return authService;
    }
}
