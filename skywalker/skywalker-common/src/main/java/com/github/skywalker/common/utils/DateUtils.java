package com.github.skywalker.common.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * @author CHAO
 */
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM_SS_SLASH = "yyyy/MM/dd HH:mm:ss";

    private DateUtils() {
    }

    /**
     * 默认格式 yyyy-MM-dd HH:mm:ss
     */
    public static String formatDefault(Date date) {
        return format(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 解析日期默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr Date
     * @return date
     */
    public static Date parseDefault(String dateStr) {
        return parse(dateStr, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 格式 yyyy-MM-dd
     */
    public static String formatYmd(Date date) {
        return format(date, YYYY_MM_DD);
    }

    /**
     * 格式 yyyy-MM-dd
     */
    public static Date parseYmd(String dateStr) {
        return parse(dateStr, YYYY_MM_DD);
    }

    /**
     * 格式 yyyyMMdd
     */
    public static String formatYmdWithoutSeparate(Date date) {
        return format(date, YYYYMMDD);
    }

    /**
     * 格式 yyyyMMdd
     */
    public static Date parseYmdWithoutSeparate(String dateStr) {
        return parse(dateStr, YYYYMMDD);
    }

    /**
     * 格式化日期
     *
     * @param date    Date
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException();
        }
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 解析日期
     *
     * @param dateStr Date
     * @param pattern 日期格式
     * @return date
     */
    @SneakyThrows
    public static Date parse(String dateStr, String pattern) {
        return FastDateFormat.getInstance(pattern).parse(dateStr);
    }

    public static Date dateOfBegin(Date date) {
        return truncate(date, Calendar.DATE);
    }

    public static Date dateOfEnd(Date date) {
        return ceiling(date, Calendar.DATE);
    }
}
