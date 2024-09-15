package com.example.backend4rate.models.entities;

import java.util.List;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name="city")
@Entity
public class CityEntity implements BaseEntity<Integer>{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    private String cityName;

    @Basic
    private String countryName;

    @Basic
    private String postalCode;

    @OneToMany(mappedBy = "city")
    private List<AddressEntity> address;


}
