package org.didierdominguez.util;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

public class ReportGenerator {
    private static ReportGenerator instance;
    private String directory;
    private File file;
    private FileWriter fileWriter;
    private PrintWriter printWriter;

    private ReportGenerator() {
        directory = System.getProperty("user.dir") + "/reports";
    }

    public static ReportGenerator getInstance() {
        if (instance == null) {
            instance = new ReportGenerator();
        }
        return instance;
    }

    private void createFolder() {
        File directory = new File(this.directory);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public void writeFile(String content, String name) {
        createFolder();
        try {
            file = new File(directory + "/" + name);
            fileWriter = new FileWriter(file, false);
            printWriter = new PrintWriter(fileWriter, true);
            printWriter.println(content);
            printWriter.close();
        } catch (IOException ex) {
        }
    }

    public void generatePDF(String fileName, String header, String[] titles, Object[] data) throws Exception {
        // PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
        String dest = directory + "/" + fileName;

        createFolder();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        // Document document = new Document(pdfDocument);
        Document document = new Document(pdfDocument, new PageSize(PageSize.A4).rotate());
        Table table = new Table(titles.length);

        Paragraph paragraph = new Paragraph(header);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setFontSize(20);
        paragraph.setMarginBottom(15);
        paragraph.setBold();
        document.add(paragraph);

        for (String title : titles) {
            Cell cell = new Cell();
            cell.add(new Paragraph(title));
            cell.setFontColor(DeviceGray.WHITE);
            cell.setBackgroundColor(new DeviceRgb(34, 34, 34));
            cell.setBorder(Border.NO_BORDER);
            cell.setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(cell);
        }

        for (Object setData : data) {
            if (setData != null) {
                Object someObject = setData;

                for (Field fieldSuper : someObject.getClass().getSuperclass().getDeclaredFields()) {
                    fieldSuper.setAccessible(true);
                    Object valueSuper = fieldSuper.get(someObject);
                    if (valueSuper != null) {
                        Cell cellSuper = new Cell();
                        cellSuper.add(new Paragraph(String.valueOf(valueSuper)));
                        cellSuper.setTextAlignment(TextAlignment.CENTER);
                        cellSuper.setBorderLeft(Border.NO_BORDER);
                        cellSuper.setBorderRight(Border.NO_BORDER);
                        cellSuper.setBorderTop(Border.NO_BORDER);
                        if (fieldSuper.getName().equals("id")) {
                            cellSuper.setFontColor(DeviceGray.WHITE);
                            cellSuper.setBackgroundColor(new DeviceRgb(34, 34, 34));
                            cellSuper.setBorderBottom(Border.NO_BORDER);
                        }
                        table.addCell(cellSuper);
                    }
                }

                for (Field field : someObject.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(someObject);
                    if (value != null) {
                        Cell cell = new Cell();
                        cell.add(new Paragraph(String.valueOf(value)));
                        cell.setTextAlignment(TextAlignment.CENTER);
                        cell.setBorderLeft(Border.NO_BORDER);
                        cell.setBorderRight(Border.NO_BORDER);
                        cell.setBorderTop(Border.NO_BORDER);
                        if (field.getName().equals("id")) {
                            cell.setFontColor(DeviceGray.WHITE);
                            cell.setBackgroundColor(new DeviceRgb(34, 34, 34));
                            cell.setBorderBottom(Border.NO_BORDER);
                        }
                        table.addCell(cell);
                    }
                }
            }
        }
        document.add(table);
        document.close();
        System.out.println(header + " PDF Created");
    }

    public void generatePDF(String fileName, String header, String[] titles, String[] data) throws Exception {
        String dest = directory + "/" + fileName;

        createFolder();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument, new PageSize(PageSize.A4).rotate());
        Table table = new Table(titles.length);

        Paragraph paragraph = new Paragraph(header);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setFontSize(20);
        paragraph.setMarginBottom(15);
        paragraph.setBold();
        document.add(paragraph);

        for (String title : titles) {
            Cell cell = new Cell();
            cell.add(new Paragraph(title));
            cell.setFontColor(DeviceGray.WHITE);
            cell.setBackgroundColor(new DeviceRgb(34, 34, 34));
            cell.setBorder(Border.NO_BORDER);
            cell.setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(cell);
        }

        for (String field : data) {
            Cell cell = new Cell();
            cell.add(new Paragraph(String.valueOf(field)));
            cell.setTextAlignment(TextAlignment.CENTER);
            cell.setBorderLeft(Border.NO_BORDER);
            cell.setBorderRight(Border.NO_BORDER);
            cell.setBorderTop(Border.NO_BORDER);
            table.addCell(cell);
        }

        document.add(table);
        document.close();
        System.out.println(header + " PDF Created");
    }
}
