package com.jd.framework.orm.util;


import java.sql.Timestamp;
import java.text.*;
import java.util.*;

import com.jd.framework.orm.exception.SystemException;
public class DateUtils {

    public static final long SECOND = 1000;
    public static final long MINITE = SECOND * 60;
    public static final long HOUR = MINITE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;

    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    //@formatter:off
	// order is like this because the SimpleDateFormat.parse does not fail with exception.
	// if it can parse a valid date out of a substring of the full string given the mask.
	// so we have to check the most complete format first, then it fails with exception.
	private static final String[] RFC822_MASKS = { 
		"EEE, dd MMM yy HH:mm:ss z", 
		"EEE, dd MMM yy HH:mm z", 
		"dd MMM yy HH:mm:ss z", 
		"dd MMM yy HH:mm z" };

	// order is like this because the SimpleDateFormat.parse does not fail with exception.
	// if it can parse a valid date out of a substring of the full string given the mask.
	// so we have to check the most complete format first, then it fails with exception.
	private static final String[] W3CDATETIME_MASKS = { 
		"yyyy-MM-dd'T'HH:mm:ss.SSSz", 
		"yyyy-MM-dd't'HH:mm:ss.SSSz", 
		"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", 
		"yyyy-MM-dd't'HH:mm:ss.SSS'z'",
		"yyyy-MM-dd'T'HH:mm:ssz", 
		"yyyy-MM-dd't'HH:mm:ssz", 
		"yyyy-MM-dd'T'HH:mm:ss'Z'", 
		"yyyy-MM-dd't'HH:mm:ss'z'", 
		"yyyy-MM-dd'T'HH:mmz", // together with logic in the parseW3CDateTime they
		"yyyy-MM'T'HH:mmz", // handle W3C dates without time forcing them to be GMT
		"yyyy'T'HH:mmz", 
		"yyyy-MM-dd't'HH:mmz", 
		"yyyy-MM-dd'T'HH:mm'Z'", 
		"yyyy-MM-dd't'HH:mm'z'", 
		"yyyy-MM-dd", 
		"yyyy-MM", 
		"yyyy" };

	private static final String[] patterns_masks = { 
		"yyyy-MM-dd HH:mm:ss,SSS", 
		"yyyy-MM-dd HH:mm:ss.SSS",
		"yyyy-MM-dd HH:mm:ss",
		"yyyy-MM-dd HH:mm", 
		"yyyy-MM-dd", 
		"yyyy/MM/dd HH:mm:ss,SSS",
		"yyyy/MM/dd HH:mm:ss.SSS", 
		"yyyy/MM/dd HH:mm:ss",
		"yyyy/MM/dd HH:mm", 
		"yyyy/MM/dd",
		"yyyyMMddHHmmss", 
		"yyyyMMdd",
		"hh:mm:ss,SSS",
		"hh:mm:ss.SSS",
		"hh:mm:ss" };
	//@formatter:on

    /**
     * 修正 new Date() 函数返回的时间是北京时间.
     * 
     * @since 2008-10-31
     */
    public static void fixedChinaTimeZone() {
        TimeZone userTimeZone = TimeZone.getTimeZone(System.getProperty("user.timezone"));
        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        if (!chinaTimeZone.hasSameRules(userTimeZone)) {
            TimeZone.setDefault(chinaTimeZone);
            System.out.println("fixed current date:" + format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * get date string use pattern
     * 
     * @param pattern
     *            see {@link java.text.SimpleDateFormat}
     * @return a date string
     */
    public static String format(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * get date string use pattern
     * 
     * @param pattern
     *            see {@link java.text.SimpleDateFormat}
     * @return a date string
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormater = new SimpleDateFormat(pattern);
        return dateFormater.format(date);
    }

    /**
     * get current date and time string
     * 
     * @return a local datetime string
     */
    public static String getNowStr() {
        return format(new Date(), FORMAT_DATE_TIME);
    }

    /**
     * get current date string
     * 
     * @return a local date string
     */
    public static String getDateStr() {
        return format(new Date(), FORMAT_DATE);
    }

    /**
     * get current time string
     * 
     * @return a local time string
     */
    public static String getTimeStr() {
        return format(new Date(), FORMAT_TIME);
    }

    /**
     * @since 2008-01-02
     */
    public static Calendar getCalendar(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c;
    }

    /**
     * @since 2008-01-02
     */
    public static Calendar getCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * @since 2008-01-02
     */
    public static Date add(Date date, int field, int diff) {
        Calendar c = getCalendar(date);
        c.add(field, diff);
        return c.getTime();
    }

    /**
     * @since 2008-01-02
     */
    public static Date add(int field, int diff) {
        return add(new Date(), field, diff);
    }

    /**
     * 得到与当前时间相差 diff 年的时间
     */
    public static Date nextYears(int diff) {
        return add(new Date(), Calendar.YEAR, diff);
    }

    /**
     * 得到与当前时间相差 diff 年的时间
     */
    public static Date nextYears(Date date, int diff) {
        return add(date, Calendar.YEAR, diff);
    }

    /**
     * 得到与当前时间相差 diff 月的时间
     */
    public static Date nextMonths(int diff) {
        return add(new Date(), Calendar.MONTH, diff);
    }

    /**
     * 得到与当前时间相差 diff 月的时间
     */
    public static Date nextMonths(Date date, int diff) {
        return add(date, Calendar.MONTH, diff);
    }

    /**
     * 得到与当前时间相差 diff 天的时间
     */
    public static Date nextDays(int diff) {
        return add(new Date(), Calendar.DATE, diff);
    }

    /**
     * 得到与当前时间相差 diff 天的时间
     */
    public static Date nextDays(Date date, int diff) {
        return add(date, Calendar.DATE, diff);
    }

    /**
     * 得到与当前时间相差 diff 小时的时间
     */
    public static Date nextHours(int diff) {
        return add(new Date(), Calendar.HOUR, diff);
    }

    /**
     * 得到与当前时间相差 diff 小时的时间
     */
    public static Date nextHours(Date date, int diff) {
        return add(date, Calendar.HOUR, diff);
    }

    /**
     * 得到与当前时间相差 diff 分的时间
     */
    public static Date nextMinutes(int diff) {
        return add(new Date(), Calendar.MINUTE, diff);
    }

    /**
     * 得到与当前时间相差 diff 分的时间
     */
    public static Date nextMinutes(Date date, int diff) {
        return add(date, Calendar.MINUTE, diff);
    }

    /**
     * 得到与当前时间相差 diff 秒的时间
     */
    public static Date nextSeconds(int diff) {
        return add(new Date(), Calendar.SECOND, diff);
    }

    /**
     * 得到与当前时间相差 diff 秒的时间
     */
    public static Date nextSeconds(Date date, int diff) {
        return add(date, Calendar.SECOND, diff);
    }

    /**
     * 清除时间，得到日期
     * 
     * @since 2007-12-10
     */
    public static Date clearTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 清除时间，得到日期
     * 
     * @since 2007-12-10
     */
    public static Date clearDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, 0);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 0);
        return c.getTime();
    }

    /**
     * 计算 d1 - d2 的天数差
     * 
     * @since 2007-12-10
     */
    public static long diffDays(Date d1, Date d2) {
        d1 = clearTime(d1);
        d2 = clearTime(d2);
        return (d1.getTime() - d2.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 计算 d1 - d2 的毫秒差
     * 
     * @since 2007-12-10
     */
    public static long diffMillis(Date d1, Date d2) {
        return d1.getTime() - d2.getTime();
    }
    /**
     * 
     * @param d1
     * @param d2
     * @return 返回值为0，两个相等；返回值大于0，d1大于d2;返回值小于0,,d1小于d2
     */
    public static Integer compare(Date d1, Date d2){
    	java.util.Calendar c1=java.util.Calendar.getInstance();
    	java.util.Calendar c2=java.util.Calendar.getInstance();
    	c1.setTime(d1);
    	c2.setTime(d2);
    	return c1.compareTo(c2);
    }
    /**
     * 得到 Calendar 类中的 Field Value.
     * 
     * @see java.util.Calendar
     * @since 2007-12-10
     */
    public static int getCalendarField(Date date, int field) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(field);
    }

    /**
     * 得到当前时间
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getTimestamp(Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    /**
     * 用指定的格式解析日期时间.
     * 
     * @param datetime
     *            时间字符串
     * @param pattern
     *            see {@link java.text.SimpleDateFormat}
     */
    public static Date parse(String datetime, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        try {
            ParsePosition pp = new ParsePosition(0);
            Date d = sdf.parse(datetime, pp);
            if (pp.getIndex() != datetime.length()) {
                d = null;
            }
            return d;
        } catch (Throwable e) {
            throw SystemException.unchecked(e);
        }
    }

    /**
     * Parses a Date out of a string using an array of masks.
     * <p/>
     * It uses the masks in order until one of them succedes or all fail.
     * <p/>
     * 
     * @param masks
     *            array of masks to use for parsing the string
     * @param sDate
     *            string to parse for a date.
     * @return the Date represented by the given string using one of the given masks. It returns <b>null</b> if it was not possible to parse the the string with any of the masks.
     */
    public static Date parseUsingMask(String[] masks, String sDate) {
        sDate = (sDate != null) ? sDate.trim() : null;
        ParsePosition pp = null;
        Date d = null;
        for (int i = 0; d == null && i < masks.length; i++) {
            DateFormat df = new SimpleDateFormat(masks[i]);
            df.setLenient(false);
            try {
                pp = new ParsePosition(0);
                d = df.parse(sDate, pp);
                if (pp.getIndex() != sDate.length()) {
                    d = null;
                }
            } catch (Exception e) {
                // try next pattern
            }
        }
        return d;
    }

    /**
     * Parses a Date out of a String with a date in RFC822 format.
     * <p/>
     * It parsers the following formats:
     * <ul>
     * <li>"EEE, dd MMM yyyy HH:mm:ss z"</li>
     * <li>"EEE, dd MMM yyyy HH:mm z"</li>
     * <li>"EEE, dd MMM yy HH:mm:ss z"</li>
     * <li>"EEE, dd MMM yy HH:mm z"</li>
     * <li>"dd MMM yyyy HH:mm:ss z"</li>
     * <li>"dd MMM yyyy HH:mm z"</li>
     * <li>"dd MMM yy HH:mm:ss z"</li>
     * <li>"dd MMM yy HH:mm z"</li>
     * </ul>
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * 
     * @param sDate
     *            string to parse for a date.
     * @return the Date represented by the given RFC822 string. It returns <b>null</b> if it was not possible to parse the given string into a Date.
     * 
     */
    public static Date parseRFC822(String sDate) {
        int utIndex = sDate.indexOf(" UT");
        if (utIndex > -1) {
            String pre = sDate.substring(0, utIndex);
            String post = sDate.substring(utIndex + 3);
            sDate = pre + " GMT" + post;
        }
        return parseUsingMask(RFC822_MASKS, sDate);
    }

    /**
     * Parses a Date out of a String with a date in W3C date-time format.
     * <p/>
     * It parsers the following formats:
     * <ul>
     * <li>"yyyy-MM-dd'T'HH:mm:ssz"</li>
     * <li>"yyyy-MM-dd'T'HH:mmz"</li>
     * <li>"yyyy-MM-dd"</li>
     * <li>"yyyy-MM"</li>
     * <li>"yyyy"</li>
     * </ul>
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * 
     * @param sDate
     *            string to parse for a date.
     * @return the Date represented by the given W3C date-time string. It returns <b>null</b> if it was not possible to parse the given string into a Date.
     * 
     */
    public static Date parseW3CDateTime(String sDate) {
        // if sDate has time on it, it injects 'GTM' before de TZ displacement
        // to
        // allow the SimpleDateFormat parser to parse it properly
        int tIndex = sDate.indexOf("T");
        if (tIndex > -1) {
            if (sDate.endsWith("Z")) {
                sDate = sDate.substring(0, sDate.length() - 1) + "+00:00";
            }
            int tzdIndex = sDate.indexOf("+", tIndex);
            if (tzdIndex == -1) {
                tzdIndex = sDate.indexOf("-", tIndex);
            }
            if (tzdIndex > -1) {
                String pre = sDate.substring(0, tzdIndex);
                int secFraction = pre.indexOf(",");
                if (secFraction > -1) {
                    pre = pre.substring(0, secFraction);
                }
                String post = sDate.substring(tzdIndex);
                sDate = pre + "GMT" + post;
            }
        } else {
            sDate += "T00:00GMT";
        }
        return parseUsingMask(W3CDATETIME_MASKS, sDate);
    }

    /**
     * Parses a Date out of a String with a date in W3C date-time format or in a RFC822 format or in a humpic-default format.
     * <p/>
     * 用尝试多种格式解析日期时间, 修改自：http://www.koders.com/java/fidDBC85D14D02AA458CE8B8A25256E176EAC6EA748.aspx
     * <p>
     * 
     * @param sDate
     *            string to parse for a date.
     * @return the Date represented by the given W3C date-time string. It returns <b>null</b> if it was not possible to parse the given string into a Date.
     */
    public static Date parse(String sDate) {
        Date d = parseW3CDateTime(sDate);
        if (d == null) {
            d = parseRFC822(sDate);
        }
        if (d == null) {
            d = parseUsingMask(patterns_masks, sDate);
        }
        if (d == null) {
            try {
                d = DateFormat.getInstance().parse(sDate);
            } catch (ParseException e) {
                d = null;
            }
        }
        return d;
    }

    /**
     * create a RFC822 representation of a date.
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * 
     * @param date
     *            Date to parse
     * @return the RFC822 represented by the given Date It returns <b>null</b> if it was not possible to parse the date.
     */
    public static String formatRFC822(Date date) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormater.format(date);
    }

    /**
     * create a W3C Date Time representation of a date.
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * 
     * @param date
     *            Date to parse
     * @return the W3C Date Time represented by the given Date It returns <b>null</b> if it was not possible to parse the date.
     */
    public static String formatW3CDateTime(Date date) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormater.format(date);
    }

    public static void main(String args[]) {
        // System.setProperty("user.timezone", "GMT+8");
        TimeZone.setDefault(TimeZone.getTimeZone("ETC/GMT-8"));
        System.out.println(DateUtils.getNowStr());
        System.out.println(DateUtils.getDateStr());
        System.out.println(DateUtils.format("'Now:' EEE, dd MMM yyyy HH:mm:ss Z"));
        System.out.println(DateUtils.getTimestamp());
        System.out.println(DateUtils.nextDays(0));
        System.out.println(DateUtils.nextDays(-1));
        System.out.println(DateUtils.nextDays(1));
        System.out.println(DateUtils.parse("2005-2-15"));
        System.out.println(DateUtils.parse("星期三, 07 十一月 2007 12:58:28 +0800"));
        System.out.println(DateUtils.parse("Tue Nov 06 12:58:28 CST 2007"));

        Date d = DateUtils.parse("2005-2-15 12:00:00");
        System.out.println(DateUtils.format(d, "yyyy-MM-dd HH:mm:ss"));

        System.out.println(System.getProperties());
    }
}
