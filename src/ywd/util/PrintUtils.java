package ywd.util;

/**
 * @author dreamingodd
 *
 */
public class PrintUtils {

    private PrintUtils() { }

    @SuppressWarnings("rawtypes")
    public static void print(Class class_, Object msg) {
        System.out.println("INFO:  " + class_.getName() + ": " + msg);
    }

    @SuppressWarnings("rawtypes")
    public static void error(Class class_, Throwable e) {
        System.out.println("ERROR: " + class_.getName() + ": " + e);
        e.printStackTrace();
    }

    @SuppressWarnings("rawtypes")
    public static void error(Class class_, String msg) {
        System.out.println("ERROR: " + class_.getName() + ": " + msg);
    }
}
