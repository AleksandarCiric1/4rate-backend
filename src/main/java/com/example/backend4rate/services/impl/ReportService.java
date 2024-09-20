package com.example.backend4rate.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;

import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.entities.MonthlyReportEntity;
import com.example.backend4rate.models.entities.ReservationEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.enums.Months;
import com.example.backend4rate.repositories.MonthlyReportRepository;
import com.example.backend4rate.repositories.ReservationRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.ReportServiceInterface;

@Service
public class ReportService implements ReportServiceInterface{
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    public ReportService(RestaurantRepository restaurantRepository, ReservationRepository reservationRepository, MonthlyReportRepository monthlyReportRepository){
        this.restaurantRepository = restaurantRepository;
        this.reservationRepository = reservationRepository;
        this.monthlyReportRepository = monthlyReportRepository;

    }

    @SuppressWarnings("deprecation")
    @Override
    public void getReport(ByteArrayOutputStream byteArrayOutputStream, Integer restaurantId, Months month, Integer year) throws BadRequestException, NotFoundException, IOException{
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException());
        if(year < restaurantEntity.getCreatedAt().getYear() 
        || (year == restaurantEntity.getCreatedAt().getYear() && month.getBrojMeseca() < restaurantEntity.getCreatedAt().getMonth())){
            throw new BadRequestException();
        }
        Document document = new Document();
        Date date = new Date();

        List<ReservationEntity> reservations = reservationRepository.findAllByRestaurant_Id(restaurantId);
        Long numOfReservations = reservations.stream().count();
        List<ReservationEntity> approvedReservations = reservationRepository.findAllByRestaurant_IdAndStatus(restaurantId, "approved");
        Long numOfApprovedReservations = approvedReservations.stream().count();
        List<ReservationEntity> deniedReservations = reservationRepository.findAllByRestaurant_IdAndStatus(restaurantId, "denied");
        Long numOfDeniedReservations = deniedReservations.stream().count();
        List<ReservationEntity> canceledReservations = reservationRepository.findAllByRestaurant_IdAndStatus(restaurantId, "canceled");
        Long numOfCanceledReservations = canceledReservations.stream().count();
        List<ReservationEntity> pendingReservations = reservationRepository.findAllByRestaurant_IdAndStatus(restaurantId, "pending");
        Long numOfPendingReservations = pendingReservations.stream().count();

        try{
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        Font titleFont  = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph title = new Paragraph(month.toString() + " report", titleFont);
        title.setAlignment(Element.ALIGN_LEFT);
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        Paragraph header = new Paragraph("Report for " + restaurantEntity.getName() + ":", headerFont);
        document.add(header);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        addTableHeaderCell(table, "All reservations", headerFont);
        addTableHeaderCell(table, "Confirmed reservations", headerFont);
        addTableHeaderCell(table, "Denied reservations", headerFont);
        addTableHeaderCell(table, "Canceled reservations", headerFont);
        addTableHeaderCell(table, "Pending reservations", headerFont);

        
        addTableCell(table, numOfReservations.toString(), font);
        addTableCell(table, numOfApprovedReservations.toString(), font);
        addTableCell(table, numOfDeniedReservations.toString(), font);
        addTableCell(table, numOfCanceledReservations.toString(), font);
        addTableCell(table, numOfPendingReservations.toString(), font);

        document.add(table);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
        String currentDate = format.format(date);
        Font dateFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Paragraph dateString = new Paragraph("Generated on: " + currentDate, dateFont);
        dateString.setAlignment(Element.ALIGN_RIGHT);
        document.add(dateString);

        document.close();
        }
        catch(DocumentException ex){
            ex.printStackTrace();
        }

        Path uploadPath = Path.of("src/main/resources/restaurants/" + restaurantId + "/reports");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            }
        Path filePath = uploadPath.resolve(month + ".pdf");

        try {
            // Upisivanje PDF sadržaja iz memorije (byteArrayOutputStream) u fajl
            Files.write(filePath, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MonthlyReportEntity monthlyReportEntity = new MonthlyReportEntity();
        monthlyReportEntity.setId(null);
        monthlyReportEntity.setDate(date);
        monthlyReportEntity.setMonth(month.toString());
        monthlyReportEntity.setNumberOfReservations(numOfReservations);
        monthlyReportEntity.setRestaurant(restaurantEntity);
        monthlyReportRepository.save(monthlyReportEntity);
    }

    private void addTableHeaderCell(PdfPTable table, String data, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(data, font));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String data, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(data, font));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }
    
}
