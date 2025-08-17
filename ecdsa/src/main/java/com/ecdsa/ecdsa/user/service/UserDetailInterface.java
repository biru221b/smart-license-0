package com.ecdsa.ecdsa.user.service;

import com.ecdsa.ecdsa.user.model.dto.LoginDto;
import com.ecdsa.ecdsa.user.model.dto.UserDetailDto;
import com.ecdsa.ecdsa.user.model.entity.UserDetail;

import java.util.List;

public interface UserDetailInterface {
    public UserDetail saveUserDetail(UserDetailDto dto);

    public List<UserDetail> getAllUser();

    LoginDto loginUser(LoginDto dto);
}
