package com.example.backend4rate.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Report;
import com.example.backend4rate.services.impl.ReportService;

@RestController
@RequestMapping("/v1/reports")
public class ReportController {
private final ReportService reportService;

public ReportController(ReportService reportService){
    this.reportService = reportService;
}

  @GetMapping("/pdf/{restaurantId}")
    public ResponseEntity<?> downloadPdf(@PathVariable Integer restaurantId,@RequestBody Report report) throws BadRequestException, NotFoundException, IOException{
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            reportService.getReport(byteArrayOutputStream, restaurantId, report.getMonth(), report.getYear());

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", report.getMonth() + "_report.pdf");

            return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).body(resource);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
