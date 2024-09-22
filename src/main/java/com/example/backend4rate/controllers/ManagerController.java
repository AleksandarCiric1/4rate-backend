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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Report;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.enums.Months;
import com.example.backend4rate.services.impl.ManagerService;
import com.example.backend4rate.services.impl.ReportService;

@RestController
@RequestMapping("/v1/manager")
public class ManagerController {
    private final ManagerService managerService;
     private final ReportService reportService;

    public ManagerController(ManagerService managerService, ReportService reportService) {
        this.managerService = managerService;
        this.reportService = reportService;
    }

    @GetMapping("/restaurant-status/{userAccountId}")
    public ResponseEntity<?> getRestaurantStatus(@PathVariable Integer userAccountId) throws NotFoundException {
        Object response = managerService.checkRestaurantStatus(userAccountId);

        if (response instanceof Restaurant) {
            return ResponseEntity.ok(response);
        }

        if (response instanceof String && response.equals("Request is being processed")) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        if (response instanceof String && "Restaurant is blocked".equals(response)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PostMapping("/pdf/{restaurantId}")
    public ResponseEntity<?> downloadPdf(@PathVariable Integer restaurantId, @RequestBody Report report)
            throws BadRequestException, NotFoundException, IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            reportService.generateReport(byteArrayOutputStream, restaurantId,
                    Months.fromNumber(Integer.parseInt(report.getMonth())),
                    report.getYear());

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", report.getMonth() + "_report.pdf");

            return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).body(resource);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
