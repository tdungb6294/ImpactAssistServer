package db.migration;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class V0019__Import_auto_parts_csv_file_data extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        Resource resourceAutoParts = new ClassPathResource("csv/auto_parts.csv");
        Resource resourceAutoPartsCategory = new ClassPathResource("csv/auto_parts_categories.csv");
        String partsSql = "INSERT INTO auto_parts_and_services (id, auto_part, description, min_price, max_price, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        String categorySql = "INSERT INTO auto_parts_and_services_categories (id, category_name, min_price, max_price) VALUES (?, ?, ?, ?)";
        try (InputStream inputStream = resourceAutoPartsCategory.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            try (PreparedStatement statement = context.getConnection().prepareStatement(categorySql)) {
                for (CSVRecord record : records) {
                    int id = Integer.parseInt(record.get(0));
                    String category = record.get(1);
                    BigDecimal minPrice = (BigDecimal) decimalFormat.parse(record.get(2));
                    BigDecimal maxPrice = (BigDecimal) decimalFormat.parse(record.get(3));

                    statement.setInt(1, id);
                    statement.setString(2, category);
                    statement.setBigDecimal(3, minPrice);
                    statement.setBigDecimal(4, maxPrice);
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        } catch (Exception e) {
            throw new SQLException("Error inserting categories csv file", e);
        }

        try (InputStream inputStream = resourceAutoParts.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            try (PreparedStatement statement = context.getConnection().prepareStatement(partsSql)) {
                for (CSVRecord record : records) {
                    int id = Integer.parseInt(record.get(0));
                    String autoPart = record.get(1);
                    String description = record.get(2);
                    BigDecimal minPrice = (BigDecimal) decimalFormat.parse(record.get(3));
                    BigDecimal maxPrice = (BigDecimal) decimalFormat.parse(record.get(4));
                    int categoryId = Integer.parseInt(record.get(5));

                    statement.setInt(1, id);
                    statement.setString(2, autoPart);
                    statement.setString(3, description);
                    statement.setBigDecimal(4, minPrice);
                    statement.setBigDecimal(5, maxPrice);
                    statement.setInt(6, categoryId);
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        } catch (Exception e) {
            throw new SQLException("Error inserting parts csv file", e);
        }
    }


}