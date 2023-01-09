package ru.plus.irbis.web.app.service.utils;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import ru.plus.irbis.web.app.model.entity.NewsTopic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.io.File.separator;
import static java.lang.String.valueOf;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
public class CsvWriter {

    private static final String[] HEADER_CSV_FILE = {"Время", "Тема", "Количество новостей"};

    private static final File DIRECTORY = new File("report-info");

    static {
        DIRECTORY.mkdir();
    }

    public static void write(List<NewsTopic> topics, String options) {
        File file = new File(DIRECTORY + separator + options + ".csv");
        try (FileWriter fileWriter = new FileWriter(file, true);
             CSVWriter writer = new CSVWriter(fileWriter)) {

            if (file.length() == 0) {
                writer.writeNext(HEADER_CSV_FILE);
            }

            List<String[]> csvData = topics.stream()
                    .map(s -> new String[]{
                            now().format(ofPattern("yyyy-MM-dd HH:mm:ss")),
                            s.getName(),
                            valueOf(s.getBodies().size())
                    })
                    .toList();

            writer.writeAll(csvData);
        } catch (IOException e) {
            log.error("Error working with csv file: " + e.getMessage());
            return;
        }
        log.info("Write report info in file: {}", file.getName());
    }
}
