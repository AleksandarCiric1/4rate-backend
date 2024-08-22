package com.example.backend4rate.models.entities;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "administrator")
@Entity
public class AdministratorEntity implements BaseEntity<Integer>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable =  false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable = false)
    private UserAccountEntity userAccount;
    
}
