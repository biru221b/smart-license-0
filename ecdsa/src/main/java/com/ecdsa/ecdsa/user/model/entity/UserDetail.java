package com.ecdsa.ecdsa.user.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String address;

    private String DOB;

    private String DOI;

    private String DOE;
    private String citizenShipNo;

    private String category;

    private String username;

    private String password;

    private String personRole;


}
