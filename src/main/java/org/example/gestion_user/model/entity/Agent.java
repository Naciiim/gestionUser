package org.example.gestion_user.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gestion_user.model.enumeration.Roles;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

private String uid;
private String lastname;
private String firstname;

@Column(unique = true)
private String email;

private String password;
private String numCin;
private String address;
private String phonenumber;
private String description;

@Lob
@Column(columnDefinition = "LONGBLOB")
private byte[] cinRectoPath;

@Lob
@Column(columnDefinition = "LONGBLOB")
private byte[] cinVersoPath;

private String birthdate;
private Long numLicence;
private Long numRegCom;
private boolean firstLogin = true;

@JsonProperty("role")  // Assurez-vous que le rôle est correctement sérialisé
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Roles role = Roles.ROLE_AGENT;

public String  generateUid() {
    if (this.uid == null || this.uid.isEmpty()) {
        // Générer un numéro aléatoire de 8 chiffres
        long randomNumber = (long) (Math.random() * 1_0000_0000L); // Génère un nombre entre 0 et 99999999
        this.uid = "AG" + String.format("%08d", randomNumber); // Format avec 8 chiffres
    }
    return this.uid;
}

}