package com.example.backend4rate.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.enums.Months;

public interface ReportServiceInterface {
    void getReport(ByteArrayOutputStream byteArrayOutputStream, Integer restaurantId, Months month, Integer year) throws BadRequestException, NotFoundException, IOException; 
}
