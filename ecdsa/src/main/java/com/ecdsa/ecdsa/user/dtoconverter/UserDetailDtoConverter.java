package com.ecdsa.ecdsa.user.dtoconverter;

import com.ecdsa.ecdsa.user.model.dto.UserDetailDto;
import com.ecdsa.ecdsa.user.model.entity.UserDetail;

public class UserDetailDtoConverter {

    public static UserDetailDto convert(UserDetail detail){
        UserDetailDto dto = new UserDetailDto();
        dto.setId(detail.getId());
        dto.setName(detail.getName());
        dto.setAddress(detail.getAddress());
        dto.setDOB(detail.getDOB());
        dto.setCategory(detail.getCategory());
        dto.setDOE(detail.getDOE());
        dto.setCitizenShipNo(detail.getCitizenShipNo());
        dto.setDOI(detail.getDOI());
        dto.setPersonRole(detail.getPersonRole());
        dto.setUsername(detail.getUsername());
        return dto;
    }

    public static UserDetail convert(UserDetailDto detail){
        UserDetail dto = new UserDetail();
        dto.setId(detail.getId());
        dto.setName(detail.getName());
        dto.setAddress(detail.getAddress());
        dto.setDOB(detail.getDOB());
        dto.setCategory(detail.getCategory());
        dto.setDOE(detail.getDOE());
        dto.setCitizenShipNo(detail.getCitizenShipNo());
        dto.setDOI(detail.getDOI());
        dto.setPassword(detail.getPassword());
        dto.setPersonRole(detail.getPersonRole());
        dto.setUsername(detail.getUsername());
        return dto;
    }
}
