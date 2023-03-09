package com.aleksandrphilimonov.practice.converter;

import com.aleksandrphilimonov.practice.dao.UserModel;
import com.aleksandrphilimonov.practice.service.UserDTO;

public class UserModelToUserDtoConverter implements Converter<UserModel, UserDTO> {
    @Override
    public UserDTO convert(UserModel source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(source.getId());
        userDTO.setEmail(source.getEmail());
        return userDTO;
    }
}
