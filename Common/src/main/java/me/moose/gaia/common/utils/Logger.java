package me.moose.gaia.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GoXLR
 * @date 10/10/2022
 */
@SuppressWarnings("all")
public class Logger {


    private final PrintStream outStream;
    private final String loggerName;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_LIME = "\u001b[32;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @Setter private boolean printDebug = false;
    private List<String> disabledDebugBrands = new ArrayList<>();

    public Logger(String name) {
        this(name, false);
    }

    public Logger(String name, boolean printDebug) {
        this.loggerName = name;
        this.printDebug = printDebug;
        this.outStream = System.out;

        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String x) {
                info(x);
            }
        });
        System.setErr(new PrintStream(System.err) {
            @Override
            public void println(String x) {
                error(x);
            }
        });
    }

    public Logger info(String info) {
        outStream.println("[INFO] [" + loggerName + "] " + info);
        return this;
    }

    public Logger info(String brand, String info) {
        outStream.println("[INFO] [" + brand + " - " + loggerName + "] " + info);
        return this;
    }
    public Logger sucess(String info) {
        outStream.println(ConsoleColors.GREEN_BRIGHT + "[INFO] " + ANSI_RESET + "[" + loggerName + "] " + info);
        return this;
    }

    public Logger sucess(String brand, String info) {
        outStream.println(ConsoleColors.GREEN_BRIGHT + "[INFO] " + ANSI_RESET + "[" + brand + " - " + loggerName + "] " + info);
        return this;
    }
    public Logger warn(String warn) {
        outStream.println(ANSI_YELLOW + "[WARNING] " + ANSI_RESET + "[" + loggerName + "] " + warn);
        return this;
    }

    public Logger warn(String brand, String warn) {
        outStream.println(ANSI_YELLOW + "[WARNING] " + ANSI_RESET + "[" + brand + " - " + loggerName + "] " + warn);
        return this;
    }

    public Logger error(String error) {
        outStream.println(ANSI_RED + "[ERROR] " + ANSI_RESET + "[" + loggerName + "] " + error);
        return this;
    }

    public Logger error(String brand, String error) {
        outStream.println(ANSI_RED + "[ERROR] " + ANSI_RESET + "[" + brand + " - " + loggerName + "] " + error);
        return this;
    }

    public Logger debug(String debug) {
        return debug(debug, DebugType.INFO);
    }
    public Logger debug(String debug, DebugType type) {
        if (printDebug) {
            outStream.println(ANSI_BLUE + "[DEBUG]" + ANSI_RESET + type.getMessage() + "[" + loggerName + "] " + debug);
        }
        return this;
    }

    public Logger debug(String brand, String debug) {
        return debug(brand, debug, DebugType.INFO);
    }
    public Logger debug(String brand, String debug, DebugType type) {
        if(disabledDebugBrands.contains(brand.toLowerCase())) {
            return this;
        }
        if (printDebug){
            outStream.println(ANSI_BLUE + "[DEBUG]" + ANSI_RESET + type.getMessage() + "[" + brand + " - " + loggerName + "] " + debug);
        }
        return this;
    }

    public void disableDebugBrand(String brand) {
        this.disabledDebugBrands.add(brand.toLowerCase());
    }
    public void enableDebugBrand(String brand) {
        this.disabledDebugBrands.remove(brand.toLowerCase());
    }
    @AllArgsConstructor @Getter
    public static enum DebugType {
        SUCCESS(ConsoleColors.GREEN_BRIGHT + "[SUCCESS]"),
        ERROR(ANSI_RED + "[ERROR]"),
        INFO(ANSI_RESET + "[INFO]");

        private String message;

        public String getMessage() {
            if(message.equals(ANSI_RESET)) {
                return message + " ";
            }
            return " " + message + ANSI_RESET + " ";
        }




    }
}
