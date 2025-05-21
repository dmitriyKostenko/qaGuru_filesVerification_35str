package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.*;

public class ZipFileParseTest {

    private final ClassLoader cl = ZipFileParseTest.class.getClassLoader();

    @Test
    @DisplayName("Проверка наличия и контента для PDF файла")
    public void parsePDFFileInZipArchiveTest() throws Exception {
        boolean pdfFile =false;
        try (InputStream inputStream = cl.getResourceAsStream("sample-zip-file.zip")) {
            assertThat(inputStream).isNotNull();
            try (ZipInputStream zis = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    if (zipEntry.getName().contains(".pdf")) {
                        pdfFile = true;
                        PDF pdf = new PDF(zis);
                        assertThat(pdf.text).contains("Lorem ipsum");
                    }
                }
                assertThat(pdfFile)
                        .as("Проверяем наличие pdf файла в архиве")
                        .isTrue();
            }
        }
    }

    @Test
    @DisplayName("Проверка наличия и контента для CSV файла")
    public void parseCSVFileInZipArchiveTest() throws Exception {
        boolean csvFile = false;
        try (InputStream inputStream = cl.getResourceAsStream("sample-zip-file.zip")) {
            assertThat(inputStream).isNotNull();
            try (ZipInputStream zis = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    if (zipEntry.getName().contains(".csv")) {
                        csvFile = true;
                        CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                        List<String[]> content = csvReader.readAll();
                        String[] row = content.get(1);
                        assertThat(row[1]).isEqualTo("Dulce");
                        assertThat(row[3]).isEqualTo("Female");
                    }
                }
                assertThat(csvFile)
                        .as("Проверяем наличие csv файла в архиве")
                        .isTrue();
            }
        }
    }

    @Test
    @DisplayName("Проверка наличия и контента для XLS файла")
    public void parseXLSFileInZipArchiveTest() throws Exception {
        boolean xlsFile = false;
        try (InputStream inputStream = cl.getResourceAsStream("sample-zip-file.zip")) {
            assertThat(inputStream).isNotNull();
            try (ZipInputStream zis = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    if (zipEntry.getName().contains(".xls")) {
                        xlsFile = true;
                        XLS xls = new XLS(zis);
                        assertThat(
                                xls.excel.getSheetAt(0)
                                        .getRow(2)
                                        .getCell(2)
                                        .getStringCellValue()
                        ).isEqualTo("Hashimoto");
                    }
                }
                assertThat(xlsFile)
                        .as("Проверяем наличие xls файла в архиве")
                        .isTrue();
            }
        }
    }
}
