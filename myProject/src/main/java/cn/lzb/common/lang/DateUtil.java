package cn.lzb.common.lang;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: huxing(xing.hu@360hqb.com)
 * Date: 11-9-16
 * Time: 下午2:27
 */
public class DateUtil {

    public static final String DATETIME_FORMAT     = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT         = "yyyy-MM-dd";

    public static final String DATE_FORMAT_CN      = "yyyy年MM月dd日HH时mm分ss秒";

    public static final String TIME_FORMAT         = "HH:mm:ss";

    /**
     * 简单格式日期
     */
    public static final String DATE_PLAIN_FORMAT   ="yyyyMMdd";

    /**
     * 简单格式时间
     */
    public static final String TIME_PLAIN_FORMAT    = "HHmmss";


    public static final long   DATE_MILLIN_SECONDS = 24 * 60 * 60 * 1000;

    /**
     * 获取当前时间戳
     * @return
     */
    public static int currentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 将当前日期格式划成"yyyy-MM-dd HH:mm:ss"的字符串
     */
    public static String formatDateTime() {
        return format(new Date(), DATETIME_FORMAT);
    }

    /**
     * 将指定的时间格式化成"yyyy-MM-dd HH:mm:ss"的字符串
     */
    public static String formatDateTime(Date date) {
        return format(date, DATETIME_FORMAT);
    }

    /**
     * 将指定的时间格式化成"yyyy-MM-dd"的字符串
     * 
     * @param date 指定的时间
     * @return "yyyy-MM-dd"的字符串
     */
    public static String formatDate(Date date) {
        return format(date, DATE_FORMAT);
    }

    /**
     * 将当前日期格式划成"yyyy-MM-dd"的字符串
     *
     * @return 日期字符
     */
    public static String formatDate() {
        return format(new Date(), DATE_FORMAT);
    }

    /**
     * 将当前时间格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 将datatme转String格试
     *
     * @param datetime
     * @return
     */
    public static String getDateTimeToString(int datetime) {
        if (datetime > 0) {
            String dateTime = new DateTime(datetime * 1000L).toString(DATETIME_FORMAT);
            return dateTime;
        }
        return "";
    }

    /**
     * 将长整形的datetime转换成字符串格式
     * 
     * @param datetime 长整形的datetime
     * @return 字符串格式的datetime
     */
    public static String getLongDateTimeToString(long datetime) {
        if (datetime > 0) {
            String dateTime = new DateTime(datetime).toString(DATETIME_FORMAT);
            return dateTime;
        }
        return "";
    }

    /**
     * 将长整形的数字转换成日期型字符串
     * 
     * @param datetime 长整形的数字
     * @return 日期型字符串
     */
    public static String parseLongToDateString(long datetime) {
        if (datetime > 0) {
            String dateTime = new DateTime(datetime).toString(DATE_FORMAT);
            return dateTime;
        }
        return "";
    }

    /**
     * 将DateTime转换成时间戳
     *
     * @param dateTime
     * @return
     */
    public static long getDateTimeToLong(DateTime dateTime) {
        return dateTime.getMillis() / 1000L;
    }

    public static String getDateTimeToCNString(int datetime) {
        if (datetime > 0) {
            String dateTime = new DateTime(datetime * 1000L).toString(DATE_FORMAT_CN);
            return dateTime;
        }
        return "";
    }

    /**
     * 获取当前年份
     * 
     * @return 当前年份
     */
    public static final int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * 
     * @return 当前月份
     */
    public static final int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前日(1-31)
     * 
     * @return 当前日(1-31)
     */
    public static final int getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    /**
     * 解析字符串(yyyy-MM-dd HH:mm:ss格式的)成日期
     * 
     * @param source 字符串
     * @return 日期
     */
    public static final Date parseDateTime(String source) {
        SimpleDateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            return df.parse(source);
        } catch (Exception e) {
            throw new RuntimeException("解析字符串成日期出现异常，source = " + source, e);
        }
    }

    /**
     * 解析指定字符串成日期
     * 
     * @param source 字符串
     * @return 日期
     */
    public static final Date parseDate(String source, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(source);
        } catch (Exception e) {
            throw new RuntimeException("解析字符串成日期出现异常，source = " + source, e);
        }
    }

    /**
     * 解析字符串(yyyy-MM-dd格式的)成日期
     * 
     * @param source 字符串
     * @return 日期
     */
    public static final Date parseDate(String source) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            return df.parse(source);
        } catch (Exception e) {
            throw new RuntimeException("解析字符串成日期出现异常，source = " + source, e);
        }
    }

    /**
     * 比较两个日期(以毫秒比较)的大小
     * 
     * @param d1 第一个日期
     * @param d2 第二个日期
     * @return 第一个大则返回1，第二个小则返回-1，相等返回0
     */
    public static final int compare(Date d1, Date d2) {
        if (d1.getTime() > d2.getTime()) {
            return 1;
        } else if (d1.getTime() < d2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 比较指定日期(以毫秒比较)和当前日期的大小
     * 
     * @param date 指定日期
     * @return 指定日期大则返回1，指定日期小则返回-1，相等返回0
     */
    public static final int compareToCurrent(Date date) {
        Date current = new Date();
        return compare(date, current);
    }

    /**
     * 比较指定长整形日期和当前日期的大小
     * 
     * @param datetime 指定长整形日期
     * @return 指定长整形日期大则返回1，指定长整形日期小则返回-1，相等返回0
     */
    public static final int compareToCurrent(long datetime) {
        Date date = new Date(datetime);
        return compareToCurrent(date);
    }

    /**
     * 获取指定日期的起始时间
     * <p>
     * 如：输入2012-12-18 12:14:19 输出 2012-12-18 00:00:00
     * </p>
     * @param date 指定日期，不能为<code>NULL</code>
     * @return 指定日期的起始时间
     */
    public static final Date getDateOfStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前日期的起始时间
     * <p>
     * 如：当前日期为2012-12-18 12:14:19 输出 2012-12-18 00:00:00
     * </p>
     * @return 指定日期的起始时间
     */
    public static final Date getCurrentDateOfStart() {
        return getDateOfStart(new Date());
    }

    /**
     * 获取指定日期的结束时间
     * <p>
     * 如：输入2012-12-18 12:14:19 输出 2012-12-18 23:59:59
     * </p>
     * @param date 指定日期，不能为<code>NULL</code>
     * @return 指定日期的结束时间
     */
    public static final Date getDateOfEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取当前日期的结束时间
     * <p>
     * 如：当前日期为2012-12-18 12:14:19 输出 2012-12-18 23:59:59
     * </p>
     * @return 指定日期的结束时间
     */
    public static final Date getCurrentDateOfEnd() {
        return getDateOfEnd(new Date());
    }

    /**
     * 获取24小时前的时间
     * <p>
     * 如：当前时间为2012-12-18 12:14:19 输出 2012-12-17 12:14:19
     * </p>
     * @return 24小时前的时间
     */
    public static final Date getYesterday() {
        return getYesterday(new Date());
    }

    /**
     * 获取指定时间的24小时前的时间
     * <p>
     * 如：指定时间为2012-12-18 12:14:19 输出 2012-12-17 12:14:19
     * </p>
     * @param date 指定时间，不能为<code>NULL</code>
     * @return 24小时前的时间
     */
    public static final Date getYesterday(Date date) {
        return getIntevalOfDate(date, -1);
    }

    /**
     * 获取24小时后的时间
     * <p>
     * 如：当前时间为2012-12-18 12:14:19 输出 2012-12-19 12:14:19
     * </p>
     * @return 24小时前的时间
     */
    public static final Date getTomorrow() {
        return getTomorrow(new Date());
    }

    /**
     * 获取指定时间的24小时后的时间
     * <p>
     * 如：指定时间为2012-12-18 12:14:19 输出 2012-12-19 12:14:19
     * </p>
     * @param date 指定时间，不能为<code>NULL</code>
     * @return 24小时后的时间
     */
    public static final Date getTomorrow(Date date) {
        return getIntevalOfDate(date, 1);
    }

    /**
     * 获取与当前时间相差指定天数*24小时的时间
     * <p>
     * 如：当前时间为2012-12-18 12:14:19，天数为2 输出 2012-12-20 12:14:19</br>
     * 当前时间为2012-12-18 12:14:19，天数为-2 输出 2012-12-16 12:14:19
     * </p>
     * @param inteval 指定天数，为整数则比指定时间晚，为负数则比指定时间早
     * @return 与当前时间相差指定天数*24小时的时间
     */
    public static final Date getIntevalOfDate(int inteval) {
        Date date = new Date();
        return getIntevalOfDate(date, inteval);
    }

    /**
     * 获取与指定时间相差指定天数*24小时的时间
     * <p>
     * 如：指定时间为2012-12-18 12:14:19，天数为2 输出 2012-12-20 12:14:19</br>
     * 指定时间为2012-12-18 12:14:19，天数为-2 输出 2012-12-16 12:14:19
     * </p>
     * @param date 指定时间，不能为<code>NULL</code>
     * @param inteval 指定天数，为整数则比指定时间晚，为负数则比指定时间早
     * @return 与指定时间相差指定天数*24小时的时间
     */
    public static final Date getIntevalOfDate(Date date, int inteval) {
        if (inteval == 0) {
            return date;
        } else {
            long time = date.getTime();
            time = time + inteval * DATE_MILLIN_SECONDS;
            return new Date(time);
        }
    }

}
