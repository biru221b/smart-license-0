package com.ecdsa.ecdsa.user.controller;


import com.ecdsa.ecdsa.user.dtoconverter.UserDetailDtoConverter;
import com.ecdsa.ecdsa.user.model.dto.LoginDto;
import com.ecdsa.ecdsa.user.model.dto.UserDetailDto;
import com.ecdsa.ecdsa.user.service.UserDetailInterface;
import com.ecdsa.ecdsa.user.service.impl.DigitalSignature;
import com.ecdsa.ecdsa.utils.ResponseMessage;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/userdetail")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserDetailController {

    @Autowired
    UserDetailInterface service;

    @Autowired
    DigitalSignature digitalSignature;

    @PostMapping
    public ResponseEntity saveUser(@RequestBody UserDetailDto dto){
        return ResponseMessage.success(UserDetailDtoConverter.convert(service.saveUserDetail(dto)));
    }

    @GetMapping
    public ResponseEntity getAllUser(){
        return ResponseMessage.success(service.getAllUser().stream().map(UserDetailDtoConverter::convert).collect(Collectors.toList()));
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginDto dto){
        return ResponseMessage.success(service.loginUser(dto));
    }

    @GetMapping("/sign/qr")
    public ResponseEntity signQr(@RequestParam("userId") int userId) throws IOException, WriterException {
        digitalSignature.generateQR(userId);
        return ResponseMessage.success();
    }


}
