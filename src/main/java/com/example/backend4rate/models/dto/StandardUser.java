package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StandardUser extends UserAccount{
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String contact;
}
