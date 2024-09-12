package com.example.backend4rate.models.dto;

import lombok.Data;
import java.util.List;

@Data
public class Restaurant {
    private Integer id;
    private String name;
    private String description;
    private String workTime;
    private String status;
    private List<String> phoneNumbers;  
    private List<String> images;        
    private List<String> categories;   
    private double averageGrade;   
}
