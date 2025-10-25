package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by win7 on 2016/6/9.
 */
@Singleton
public class DateUtils {

    public static final long ARTICLE_END_TIME = 4102243200l;

    /**
     * 获取当前时间，以秒为单位
     *
     * @return
     */
    public long getCurrentTimeByMills() {
        return System.currentTimeMillis();
    }

    int pageSize = 0;

    /**
     * 秒数转化为HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public String formatSecondsToHMS(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 秒数格式化为年月日,如20160503
     *
     * @param timeStamp
     * @return
     */
    public String formatToYMD(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 秒数转化为yyyy-MM-DD HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public String formatToYMDHMSBySecond(long timeStamp) {
        if (timeStamp < 1) return "";
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 秒数转化为yyyy-MM-DD HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public String formatToYMBySecond(long timeStamp) {
        if (timeStamp < 1) return "";
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 秒数转化为yyyy-MM-DD HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public String formatToDayBySecond(long timeStamp) {
        if (timeStamp < 1) return "";
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("d");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 秒数转化为MM-DD HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public String formatToMDHMSBySecond(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 秒数转化为MM-DD
     *
     * @param timeStamp
     * @return
     */
    public String formatToMDBySecond(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("MM月dd号");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 秒数转化为yyyy-MM-DD
     *
     * @param timeStamp
     * @return
     */
    public String formatToDashYMDBySecond(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * 获取昨夜零点的秒数
     *
     * @return
     */
    public long getTodayMinTimestamp() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayMin = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0, 0);
        Timestamp timestamp = Timestamp.valueOf(todayMin);
        return timestamp.getTime();
    }


    /**
     * 获取今夜零点的秒数
     *
     * @return
     */
    public long getTodayMidnightTimestamp() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime todayMidNight = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 23, 59, 59);
        Timestamp timestamp = Timestamp.valueOf(todayMidNight);
        return timestamp.getTime();
    }

    /**
     * 获取今夜零点的秒数
     *
     * @return
     */
    public long get3DaysAgoTimestamp() {
        return getCurrentTimeByMills() - 3 * 24 * 3600 * 1000;
    }

    /**
     * 获取多少天前的秒数
     *
     * @return
     */
    public long getDaysAgoTimestamp(int days) {
        return getCurrentTimeByMills() - days * 24 * 3600 * 1000;
    }

    /**
     * 输出距离今天天数的某一天时间，格式以2016-03-20输出
     *
     * @param farFrom
     * @return
     */
    public String getFormatdayFarfromToday(int farFrom) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(farFrom);
        String formatString = yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return formatString;
    }

    /**
     * 获取当前时间的秒数，取整到分钟
     *
     * @return
     */
    public long getCurrentTimeTrunkMinutes() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now.truncatedTo(ChronoUnit.MINUTES));
        long currentTimeWithoutMinutes = timestamp.getTime() / 1000;
        return currentTimeWithoutMinutes;
    }

    /**
     * 获取当前时间的秒数，取整到小时
     *
     * @return
     */
    public long getCurrentTimeTrunkHours() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now.truncatedTo(ChronoUnit.HOURS));
        long currentTimeWithoutHours = timestamp.getTime() / 1000;
        return currentTimeWithoutHours;
    }

    /**
     * 转换时间
     *
     * @param date
     * @return
     */
    @NotNull
    public String formateStringToDateYMD(String date) {
        String[] split = date.split("-");
        LocalDate thatDate = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        return thatDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public long convertMonthToMinUnixStamp(String date) {
        String[] split = date.split("-");
        LocalDateTime thatDate = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 1, 0, 0, 0);
        Timestamp timestamp = Timestamp.valueOf(thatDate);
        return timestamp.getTime();
    }

    public long convertMonthToMaxUnixStamp(String date) {
        String[] split = date.split("-");
        LocalDateTime thatDate = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 1, 0, 0, 0);
        Timestamp timestamp = Timestamp.valueOf(thatDate.plusMonths(1));
        return timestamp.getTime();
    }

    /**
     * 日期转为unix stamp
     *
     * @param date
     * @return
     */
    public long convertStringToUnixStamp(String date) {
        String[] split = date.split("-");
        LocalDateTime time = LocalDateTime.of(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                0, 0, 0);
        Timestamp timestamp = Timestamp.valueOf(time);
        return timestamp.getTime();
    }

    public long convertYMDHMS2unixstamp(String date) {
        if (ValidationUtil.isEmpty(date)) return 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date result = df.parse(date);
            return result.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String convertToHHmmssSSS(long createTime) {
        DateFormat formatter = new SimpleDateFormat("HHmmssSSS");
        String dateFormatted = formatter.format(createTime);
        return dateFormatted;
    }

    public String convertToDateWithMills(long createTime) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateFormatted = formatter.format(createTime);
        return dateFormatted;
    }

    /**
     * 获取昨日时间戳最小值与最大值
     *
     * @return
     */
    public long[] getYesterDayMinAndMaxStamp() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime yesterdayMin = LocalDateTime.of(yesterday.getYear(), yesterday.getMonth(), yesterday.getDayOfMonth(), 0, 0, 0);
        LocalDateTime yesterdayMax = LocalDateTime.of(yesterday.getYear(), yesterday.getMonth(), yesterday.getDayOfMonth(), 23, 59, 59);
        long minStamp = Timestamp.valueOf(yesterdayMin).getTime();
        long maxStamp = Timestamp.valueOf(yesterdayMax).getTime();
        return new long[]{minStamp, maxStamp};
    }

    /**
     * 秒数转化为yyyy-MM-DD HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public String formatToYMDDashBySecond(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public String getNowYMDHMS() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public String getCurrentMonth() {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        String monthStr = month + "";
        if (month < 10) monthStr = "0" + month;
        return now.getYear() + monthStr;
    }

    public String getTodayDash() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateFormatted = now.format(formatter);
        return dateFormatted;
    }

    public String getToday() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateFormatted = now.format(formatter);
        return dateFormatted;
    }

    public static String secondToTime(long second) {
        if (second <= 0) return "已结束";
        long days = second / 86400;            //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;                //剩余秒数
        if (days > 0) {
            return days + "天" + hours + "小时" + minutes + "分" + second + "秒";
        } else {
            return hours + "小时" + minutes + "分" + second + "秒";
        }
    }


    public String getCurrentTimeWithHMS() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss");
        String dateFormatted = now.format(formatter);
        return dateFormatted;
    }


    public int getCurrentHour() {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour();
    }


    public boolean isUnixStampToday(long birthday) {
        Timestamp timestamp = Timestamp.from(Instant.ofEpochSecond(birthday));
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.getMonthValue() == now.getMonthValue()
                && localDateTime.getDayOfMonth() == now.getDayOfMonth())
            return true;
        else return false;
    }

    public long convertYMDHM2Timestamp(String date) {
        if (ValidationUtil.isEmpty(date)) return 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date result = df.parse(date);
            return result.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getNowYMTDHMS() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public String getLastMonth() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        int month = lastMonth.getMonthValue();
        String monthStr = month + "";
        if (month < 10) monthStr = "0" + month;
        return lastMonth.getYear() + monthStr;
    }


    private ObjectNode toDateObject(long beginTime) {
        ObjectNode node = Json.newObject();
        node.put("timestamp", beginTime);
        String[] timeStr = convertTimeToStr(beginTime);
        node.put("timeStr", timeStr[0] + "-" + timeStr[1]);
        return node;
    }

    private String[] convertTimeToStr(long time) {
        return new String[]{getMinSecond(time), getMinSecond(time + 30 * 60)};
    }

    private String getMonthDate(long timestamp) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayMaxTimestamp = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 23, 59, 59);
        long todayMax = Timestamp.valueOf(todayMaxTimestamp).getTime();
        long tomorrowMax = todayMax + 24 * 3600 * 1000;
        Date date = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat("MM-dd");
        String day = formatter.format(date);
        if (timestamp < todayMax) {
            return day + "(今天)";
        } else if (timestamp < tomorrowMax) {
            return day + "(明天)";
        } else {
            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(8));
            DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
            String[] weeks = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
            String weekName = weeks[dayOfWeek.getValue() - 1];
            return day + weekName;
        }
    }

    private String getMinSecond(long timestamp) {
        Date date = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }


    public String getTodayDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateFormatted = now.format(formatter);
        return dateFormatted;
    }
}