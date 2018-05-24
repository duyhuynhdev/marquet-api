package com.marqet.WebServer.util;

import org.apache.log4j.*;

import java.io.File;

/**
 * Created by hpduy17 on 12/2/14.
 */
public class LoggerFactory {
    public static Logger createLogger(Class c) {
        Logger logger = Logger.getLogger(c);
        try {
            // ClassLoader classLoader = LoggerFactory.class.getClassLoader();

            // System.setProperty("log4j.configuration", new File(classLoader.getResource("log4j.properties").getFile()).toURL().toString());
            //System.setProperty("my.log",logPath);
            Layout layout = new PatternLayout("%d{yyyy/MM/dd hh:mm:ss} %5p %c{1}:%L - %m%n");
            String logFilePath = Path.getLogPath()+ File.separator+"marqet.log";
            File logFile = new File(logFilePath);
            if(!logFile.exists())
                logFile.createNewFile();
            FileAppender appender = new FileAppender(layout, logFilePath, true);
            logger.addAppender(appender);
            logger.setLevel(Level.INFO);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logger;
    }
}
