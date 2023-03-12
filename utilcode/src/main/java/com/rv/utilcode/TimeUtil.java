package com.rv.utilcode;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.rv.utilcode.constant.TimeConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * author : rv
 * github : https://github.com/Mac-sir
 * time   : 2023/3/7
 * 主要功能 : 获取时间和时间转换
 */
public class TimeUtil {

    private static final ThreadLocal<Map<String, SimpleDateFormat>> SDF_THREAD_LOCAL
            = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<>();
        }
    };

    private static SimpleDateFormat getDefaultFormat() {
        return getSafeDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 检验当前是否是网络时间
     *
     * @return 是否网络时间
     */
    public static boolean isUsingNetworkProvidedTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(AppUtils.getApp().getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(AppUtils.getApp().getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getSafeDateFormat(String pattern) {
        Map<String, SimpleDateFormat> sdfMap = SDF_THREAD_LOCAL.get();
        //noinspection ConstantConditions
        SimpleDateFormat simpleDateFormat = sdfMap.get(pattern);
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern);
            sdfMap.put(pattern, simpleDateFormat);
        }
        return simpleDateFormat;
    }

    ////////////////////////////////////////////////////////////////////
    //                     常用时间方法
    ////////////////////////////////////////////////////////////////////

    /**
     * 获取当前时间
     *
     * @return long时间戳
     */
    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     *
     * @return 返回"yyyy-MM-dd HH:mm:ss"格式的字符串时间
     */
    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), getDefaultFormat());
    }

    /**
     * 获取当前时间
     * @return 返回date类型数据
     */
    public static Date getNowDate() {
        return new Date();
    }
    ////////////////////////////////////////////////////////////////////
    //                       long时间戳 转 String日期格式
    ////////////////////////////////////////////////////////////////////

    /**
     * @param millis long类型时间戳
     * @return long转为string
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, getDefaultFormat());
    }

    /**
     * @param millis  long类型时间戳
     * @param pattern yyyy-MM-dd HH:mm:ss
     * @return long转为string
     */
    public static String millis2String(long millis, @NonNull final String pattern) {
        return millis2String(millis, getSafeDateFormat(pattern));
    }

    /**
     * @param millis long类型时间戳
     * @param format 转换工具
     * @return long转为string
     */
    public static String millis2String(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }

    ////////////////////////////////////////////////////////////////////
    //                       String日期格式 转 long时间戳
    ////////////////////////////////////////////////////////////////////

    public static long string2Millis(final String time) {
        return string2Millis(time, getDefaultFormat());
    }

    public static long string2Millis(final String time, @NonNull final String pattern) {
        return string2Millis(time, getSafeDateFormat(pattern));
    }

    public static long string2Millis(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    ////////////////////////////////////////////////////////////////////
    //                     String日期格式 转 Date格式
    ////////////////////////////////////////////////////////////////////

    public static Date string2Date(final String time) {
        return string2Date(time, getDefaultFormat());
    }

    public static Date string2Date(final String time, @NonNull final String pattern) {
        return string2Date(time, getSafeDateFormat(pattern));
    }

    public static Date string2Date(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////
    //                     Date格式  转  String日期格式
    ////////////////////////////////////////////////////////////////////

    public static String date2String(final Date date) {
        return date2String(date, getDefaultFormat());
    }

    public static String date2String(final Date date, @NonNull final String pattern) {
        return getSafeDateFormat(pattern).format(date);
    }


    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }


    ////////////////////////////////////////////////////////////////////
    //                     Date格式  转  long时间戳
    ////////////////////////////////////////////////////////////////////

    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    ////////////////////////////////////////////////////////////////////
    //                     long时间戳  转  Date格式
    ////////////////////////////////////////////////////////////////////

    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    ////////////////////////////////////////////////////////////////////
    //                     上午、下午判断
    ////////////////////////////////////////////////////////////////////
    /**
     * Return the friendly time span by now.
     *
     * @param millis The milliseconds.
     * @return the friendly time span by now
     * <ul>
     * <li>如果小于 1 秒钟内，显示刚刚</li>
     * <li>如果在 1 分钟内，显示 XXX秒前</li>
     * <li>如果在 1 小时内，显示 XXX分钟前</li>
     * <li>如果在 1 小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
            return String.format("%tc", millis);
        if (span < 1000) {
            return "刚刚";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN);
        }
        // 获取当天 00:00
        long wee = getWeeOfToday();
        if (millis >= wee) {
            return String.format("今天%tR", millis);
        } else if (millis >= wee - TimeConstants.DAY) {
            return String.format("昨天%tR", millis);
        } else {
            return String.format("%tF", millis);
        }
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
