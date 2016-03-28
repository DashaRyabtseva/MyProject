package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public class Log implements Logger {

    private static final String TEMPLATE = "[%s] %s";

    private String tag;

    private Log(Class<?> cls) {
        tag = String.format(TEMPLATE, cls.getName(), "%s");
    }

    @Override
    public void info(String message) {
        try(FileWriter log = new FileWriter("log.txt", true);) {
            log.write(new Date().toString());
            log.write(String.format(tag, message) + "\n");
        }
        catch (IOException e) {}
        System.out.println(String.format(tag, message));
    }

    @Override
    public void error(String message, Throwable e) {
        try(FileWriter log = new FileWriter("log.txt", true);) {
            log.write(new Date().toString());
            log.write(String.format(tag, message) + "\n");
        }
        catch (IOException d) {}
        System.err.println(String.format(tag, message));
        e.printStackTrace(System.err);
    }

    public static Log create(Class<?> cls) {
        return new Log(cls);
    }
}
