package com.back.web.furniture.Service;

import com.back.web.furniture.Dto.UserDto;

public interface ServiceUser {
    public UserDto getUser(String username);
    public UserDto updateUser(UserDto userDto);
}
