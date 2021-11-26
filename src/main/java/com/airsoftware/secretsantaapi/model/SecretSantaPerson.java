package com.airsoftware.secretsantaapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class SecretSantaPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = {"members"}, allowSetters = true)
    private SecretSantaGroup group;

    @OneToOne
    @JsonIgnoreProperties(value = {"givesTo"}, allowSetters = true)
    private SecretSantaPerson givesTo;

}
