package ywd.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 
 * @author dreamingodd
 *
 */
public class CalendarUtils {

    public static final String YM = "yyyy-MM";
    public static final String YMD = "yyyy-MM-dd";
    public static final String HMS = "HH:mm:ss";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMSS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String YMD_CN = "yyyy年MM月dd";
    public static final String YMDHMS_CN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String HMS_CM = "HH时mm分SS秒";

    private CalendarUtils() { }

    public static Date getDate(int year) {
        return getDate(year, 1, 1, 0, 0, 0);
    }

    public static Date getDate(int year, int month) {
        return getDate(year, month, 1, 0, 0, 0);
    }

    public static Date getDate(int year, int month, int day) {
        return getDate(year, month, day, 0, 0, 0);
    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, hour, minute, second);
        long l = c.getTime().getTime();
        l = l / 1000;
        l = l * 1000;
        c.setTimeInMillis(l);
        return c.getTime();
    }

    /**
     * format, dateStr
     * @param format
     * @param dateStr
     */
    public static Date parseDate(String formatStr, String dateStr)throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.parse(dateStr);
    }

    public static String toString(String formatStr, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * 1月1日，2014
     * @param dateStr
     * @param yearStr
     * @return
     */
    public static Date parseExcelDate(String dateStr, String yearStr) {
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(dateStr.split("月")[0]);
        int day = Integer.parseInt(dateStr.split("月")[0].replace("日", ""));
        return getDate(year, month, day);
    }

    public static Date parseExcelMonth(String monthStr, String yearStr) {
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr.replace("月", ""));
        return getDate(year, month);
    }

    public static void main(String[] args) {
        System.out.println(getDate(2014).getTime());
        System.out.println(getDate(2014, 1).getTime());
        System.out.println(getDate(2014, 1, 1, 0, 0, 2).getTime());
        System.out.println(parseExcelDate("10月10日", "2014"));
    }
}
