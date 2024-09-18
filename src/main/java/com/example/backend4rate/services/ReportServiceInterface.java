package com.example.backend4rate.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;

public interface ReportServiceInterface {
    void getReport(ByteArrayOutputStream byteArrayOutputStream, Integer restaurantId, Integer month, Integer year) throws BadRequestException, NotFoundException, IOException; 
}
