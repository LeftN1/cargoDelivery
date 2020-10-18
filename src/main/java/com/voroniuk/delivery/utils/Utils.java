package com.voroniuk.delivery.utils;

import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.Total;
import com.voroniuk.delivery.web.command.ReportCommand;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Utils {

    private static final Logger LOG = Logger.getLogger(ReportCommand.class);

    public static int getPageNoFromRequest(HttpServletRequest req, String paramName, int totalPages) {
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(req.getParameter(paramName));
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }
        return pageNo;
    }

    public static void createXLS(Map<String, List<Delivery>> report, Map<String, Total> totals, Locale locale, String name) {

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report");
        CreationHelper createHelper = workbook.getCreationHelper();

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(font);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("d.m.yyyy"));

        int rowN = 0;
        Cell cell;
        Row row;

        row = sheet.createRow(rowN);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.id"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.origin"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.destination"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.adress"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.cargo_type"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.weight"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.volume"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.cost"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.status"));
        cell.setCellStyle(boldStyle);
        cell = row.createCell(9, CellType.STRING);
        cell.setCellValue(rb.getString("all.label.date"));
        cell.setCellStyle(boldStyle);

        for (Map.Entry<String, List<Delivery>> entry : report.entrySet()) {
            rowN++;
            row = sheet.createRow(rowN);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(entry.getKey());
            cell.setCellStyle(boldStyle);

            for (Delivery delivery : entry.getValue()) {
                rowN++;
                row = sheet.createRow(rowN);

                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(delivery.getId());

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(delivery.getOrigin().getName(locale));

                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(delivery.getDestination().getName(locale));

                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(delivery.getAdress());

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(delivery.getType().getName(locale));

                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue(delivery.getWeight());

                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue(delivery.getVolume());

                cell = row.createCell(7, CellType.NUMERIC);
                cell.setCellValue(delivery.getCost());

                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue(delivery.getLastStatus().getName(locale));

                cell = row.createCell(9, CellType.NUMERIC);
                cell.setCellStyle(dateStyle);
                cell.setCellValue(delivery.getLastDate());
            }
            rowN++;
            row = sheet.createRow(rowN);

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(rb.getString("report.label.total"));
            cell.setCellStyle(boldStyle);

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue(totals.get(entry.getKey()).getTotalWeight());
            cell.setCellStyle(boldStyle);

            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellValue(totals.get(entry.getKey()).getTotalVolume());
            cell.setCellStyle(boldStyle);

            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellValue(totals.get(entry.getKey()).getTotalCost());
            cell.setCellStyle(boldStyle);

        }

        File file = new File("src\\main\\webapp\\reports\\"+name);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("Created file: " + file.getAbsolutePath());


    }

}
