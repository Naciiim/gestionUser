package org.example.gestion_user.model.dto;

import lombok.Data;

@Data
public class AgentDto{
    private Long id;
    private String uid;
    private String lastname;
    private String firstname;
    private String email;
    private String password;
    private String numCin;
    private String address;
    private String phonenumber;
    private String description;
    private byte[] cinRectoPath;
    private byte[] cinVersoPath;
    private String birthdate;
    private Long numLicence;
    private Long numRegCom;
    private boolean firstLogin;
}
