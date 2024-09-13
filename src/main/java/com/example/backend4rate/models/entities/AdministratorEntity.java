package com.example.backend4rate.models.entities;

import java.util.Date;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "administrator")
@Entity
public class AdministratorEntity implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Basic
    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    @OneToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable = false)
    private UserAccountEntity userAccount;

}
