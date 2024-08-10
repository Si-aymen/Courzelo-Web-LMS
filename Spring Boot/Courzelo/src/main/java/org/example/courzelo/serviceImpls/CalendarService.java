package org.example.courzelo.serviceImpls;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.example.courzelo.dto.requests.CalendarEventRequest;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;

@Service
public class CalendarService {

    public void createCalendarSheet(Workbook workbook, List<CalendarEventRequest> events) {
        Sheet sheet = workbook.createSheet("School Year Calendar");
        CellStyle cellStyle = createCellStyle(workbook);
        String[] MONTHS = {
                "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"
        };
        CellStyle headerStyle = createHeaderStyle(workbook);
        Font font = workbook.createFont();
        styleMonthHeader(headerStyle, font);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < MONTHS.length; i++) {
            int startColIndex = i * 3;
            int endColIndex = (i * 3) + 2;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, startColIndex, endColIndex));
            Cell headerCell = headerRow.createCell(startColIndex);
            headerCell.setCellValue(MONTHS[i]);
            headerCell.setCellStyle(headerStyle);
            createDaysInMonth(sheet, i, cellStyle);
            createEvents(events, sheet, i, workbook,font);
        }
    }

    private CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    private CellStyle createEventStyle(Workbook workbook, CalendarEventRequest event) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        java.awt.Color awtColor = java.awt.Color.decode(event.getColor());
        XSSFColor color = new XSSFColor(new java.awt.Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue()), new DefaultIndexedColorMap());
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    private void styleEvent(CellStyle style, Font font) {
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
    }
    private void styleMonthHeader(CellStyle style, Font font) {
        font.setBold(true);
        font.setFontHeightInPoints((short) 15);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
    }
    private void createDaysInMonth(Sheet sheet, int monthIndex, CellStyle style) {
        int daysInMonth = YearMonth.now().withMonth(monthIndex + 1).lengthOfMonth();
        for (int j = 1; j <= daysInMonth; j++) {
            Row daysRow = sheet.getRow(j);
            if (daysRow == null) {
                daysRow = sheet.createRow(j);
            }
            Cell daysCell = daysRow.createCell(monthIndex * 3);
            daysCell.setCellValue(j);
            daysCell.setCellStyle(style);
        }
    }

    private void createEvents(List<CalendarEventRequest> events, Sheet sheet, int monthIndex, Workbook workbook,Font font) {
        Calendar cal = Calendar.getInstance();
        for (CalendarEventRequest event : events) {
            cal.setTime(event.getStartDate());
            Calendar finishCal = Calendar.getInstance();
            finishCal.setTime(event.getFinishDate());
            if (cal.get(Calendar.MONTH)+1 == monthIndex + 1) {
                int startRowIndex = cal.get(Calendar.DAY_OF_MONTH);
                int endRowIndex = finishCal.get(Calendar.DAY_OF_MONTH);
                for (int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++) {
                    int columnIndex = monthIndex * 3 + 1;
                    Row eventRow = sheet.getRow(rowIndex);
                    if (eventRow == null) {
                        eventRow = sheet.createRow(rowIndex);
                    }
                    Cell eventCell = eventRow.createCell(columnIndex);
                    eventCell.setCellValue(event.getName());
                    CellStyle eventStyle = createEventStyle(workbook, event);
                    styleEvent(eventStyle, font);
                    eventCell.setCellStyle(eventStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex, endRowIndex, monthIndex * 3 + 1, monthIndex * 3 + 2));
            }
        }
    }
}
