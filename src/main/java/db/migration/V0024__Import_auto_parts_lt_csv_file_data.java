package db.migration;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class V0024__Import_auto_parts_lt_csv_file_data extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        Resource resourceAutoParts = new ClassPathResource("csv/auto_parts_lt.csv");
        Resource resourceAutoPartsCategory = new ClassPathResource("csv/auto_parts_categories_lt.csv");
        String partsSql = "INSERT INTO auto_parts_and_services_translations (id, auto_part, description, language_code) VALUES (?, ?, ?, ?)";
        String categorySql = "INSERT INTO auto_parts_and_services_categories_translations (id, category_name, language_code) VALUES (?, ?, ?)";
        try (InputStream bomInputStream = resourceAutoPartsCategory.getInputStream()) {
            InputStream inputStream = skipBOM(bomInputStream);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            try (PreparedStatement statement = context.getConnection().prepareStatement(categorySql)) {
                for (CSVRecord record : records) {
                    int id = Integer.parseInt(record.get(0));
                    String category = record.get(1);
                    String languageCode = record.get(2);

                    statement.setInt(1, id);
                    statement.setString(2, category);
                    statement.setString(3, languageCode);
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        } catch (Exception e) {
            throw new SQLException("Error inserting categories translations csv file", e);
        }

        try (InputStream bomInputStream = resourceAutoParts.getInputStream()) {
            InputStream inputStream = skipBOM(bomInputStream);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            try (PreparedStatement statement = context.getConnection().prepareStatement(partsSql)) {
                for (CSVRecord record : records) {
                    int id = Integer.parseInt(record.get(0));
                    String autoPart = record.get(1);
                    String description = record.get(2);
                    String languageCode = record.get(3);

                    statement.setInt(1, id);
                    statement.setString(2, autoPart);
                    statement.setString(3, description);
                    statement.setString(4, languageCode);
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        } catch (Exception e) {
            throw new SQLException("Error inserting parts translations csv file", e);
        }
    }

    private InputStream skipBOM(InputStream inputStream) throws IOException {
        byte[] bom = new byte[3];
        inputStream.mark(3);

        int bytesRead = inputStream.read(bom);
        if (bytesRead == 3 && bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
            inputStream.reset();
            inputStream.skip(3);
        } else {
            inputStream.reset();
        }
        return inputStream;
    }
}
