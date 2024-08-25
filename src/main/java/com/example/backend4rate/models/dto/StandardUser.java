package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StandardUser extends UserAccount{
    private String name;
    private String lastname;
    private Date dateOfBirth;
    private String contact;
}
