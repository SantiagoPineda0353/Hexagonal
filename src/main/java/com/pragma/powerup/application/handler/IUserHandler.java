package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.SaveUserRequestDto;

public interface IUserHandler {
    void saveOwner(SaveUserRequestDto saveUserRequestDto);
}
