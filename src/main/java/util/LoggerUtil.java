package util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {

    private static final String LOG_FILE_PATTERN = "system_logs.%g.log";
    private static final int LOG_FILE_SIZE = 1024 * 1024; // 1 MB
    private static final int LOG_FILE_COUNT = 10;

    static {
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE_PATTERN, LOG_FILE_SIZE, LOG_FILE_COUNT, true);
            fileHandler.setFormatter(new SimpleFormatter());
            Logger rootLogger = Logger.getLogger("");
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
