package com.ecdsa.ecdsa.user.service.impl;

import com.ecdsa.ecdsa.user.dtoconverter.UserDetailDtoConverter;
import com.ecdsa.ecdsa.user.model.dto.LoginDto;
import com.ecdsa.ecdsa.user.model.dto.UserDetailDto;
import com.ecdsa.ecdsa.user.model.entity.UserDetail;
import com.ecdsa.ecdsa.user.repository.UserDetailRepository;
import com.ecdsa.ecdsa.user.service.UserDetailInterface;
import com.ecdsa.ecdsa.utils.SendErrorMessageCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailInterface {

    @Autowired
    UserDetailRepository repository;

    @Override
    public UserDetail saveUserDetail(UserDetailDto dto){
        return repository.save(UserDetailDtoConverter.convert(dto));
    }

    @Override
    public List<UserDetail> getAllUser(){
        return repository.findAll();
    }

    public LoginDto loginUser(LoginDto dto){
        try {
            UserDetail userDetails = repository.loginUser(dto.getUsername(),dto.getPassword()).get(0);
            LoginDto loginDto = new LoginDto();
            loginDto.setUsername(userDetails.getUsername());
            return loginDto;
        }catch (IndexOutOfBoundsException ex){
            throw new SendErrorMessageCustom("Invalid Username or Password");
        }


    }
}
