package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Logging {
    static void write_logging_information(String text) {
        if (log_file_exists) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                String date_text_nl = "[" +
                        dateFormat.format(date) +
                        "] " +
                        text +
                        "\n";

                Files.write(Paths.get("log.txt"), date_text_nl.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ignored) {

            }
        }
    }

    static void create_log_file() {
        try {
            File file = new File("log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Logging.log_file_exists = true;
        } catch (IOException e) {
            Logging.log_file_exists = false;
        }
    }

    private static boolean log_file_exists = false;
}
