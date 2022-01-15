package ru.zavrichko;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFilesTest {
    @Test
    void zipTest() throws Exception {
        ZipFile zipFile = new ZipFile("C:\\Users\\Andrey\\IdeaProjects\\qaguru_homework_07\\src\\test\\resources\\ziptest.zip");

        // Проверка csv файла
        ZipEntry zipCsvEntry = zipFile.getEntry("sampleEcwid.csv");
        try (InputStream CsvStream = zipFile.getInputStream(zipCsvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(CsvStream));
            List<String[]> list = reader.readAll();
            assertThat(list)
                    .hasSize(2)
                    .contains(
                            new String[]{"name", "sku", "subtitle"},
                            new String[]{"Billabong", "M115GTLE", "Pre-order available"});
        }

        // Проверка PDF файла
        ZipEntry zipPdfEntry = zipFile.getEntry("report.pdf");
        try (InputStream pdfStream = zipFile.getInputStream(zipPdfEntry)) {
            PDF parsed = new PDF(pdfStream);
            assertThat(parsed.author).contains("Эдуард");
        }

        // Проверка xlsx файла
        ZipEntry zipXlsxEntry = zipFile.getEntry("employee.xlsx");
        try (InputStream stream = zipFile.getInputStream(zipXlsxEntry)) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(1).getCell(2).getStringCellValue())
                    .isEqualTo("Инженер 1-й категории");
        }
    }
}


