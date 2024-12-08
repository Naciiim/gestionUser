package org.example.gestion_user.model.dto;

import lombok.Data;
import org.example.gestion_user.model.enumeration.AccountType;

@Data
public class ClientDto {
    private Long id;
    private String lastname;
    private String firstname;
    private String email;
    private String phonenumber;
    private String password;
    private byte[] cinRectoPath;
    private byte[] cinVersoPath;
    private String accountType;
    private boolean firstLogin;

}
