package com.airsoftware.secretsantaapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
public class SecretSantaGroup {

    @Id
    private String email;

    private String address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SecretSantaPerson> members;

}
