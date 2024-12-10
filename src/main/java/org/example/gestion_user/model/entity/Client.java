package org.example.gestion_user.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gestion_user.model.enumeration.AccountType;
import org.example.gestion_user.model.enumeration.Roles;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastname;
    private String firstname;
    private String email;
    private String phonenumber;

    private String password;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] cinRectoPath;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] cinVersoPath;

    private boolean firstLogin = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role = Roles.ROLE_CLIENT;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

}
