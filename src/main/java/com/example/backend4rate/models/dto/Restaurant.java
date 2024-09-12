package com.example.backend4rate.models.dto;

import lombok.Data;
import java.util.List;

@Data
public class Restaurant {
    private Integer id;
    private String name;
    private String description;
    private String workTime;
    private String managerName;
    private List<String> restaurantPhones;
    private List<String> images;
    private List<String> categories;
    private List<String> comments; 
    private Double averageGrade;   
}
