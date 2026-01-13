package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author sharkCode
 * @date 2026/1/13 10:52
 */
public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}
