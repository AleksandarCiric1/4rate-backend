package com.example.backend4rate.configs;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend4rate.models.dto.Category;
import com.example.backend4rate.models.entities.CategoryEntity;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration  {

    PropertyMap<Category, CategoryEntity> categoryMap = new PropertyMap<Category, CategoryEntity>(){
        @Override
        protected void configure() {
            map().setName(source.getName());
        }
    };

   @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(categoryMap);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
