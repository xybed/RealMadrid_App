package lib.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat dateFormater3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final static SimpleDateFormat dateFormater4 = new SimpleDateFormat("HH:mm");
    private final static SimpleDateFormat dateFormater5 = new SimpleDateFormat("MM-dd");

    /**
     * @param time yyyy MM dd HH:mm:ss
     * @return yyyy MM dd 星期日
     */
    public static String getTime(String time) {
        String[] days = {"日", "一", "二", "三", "四", "五", "六"};
        try {
            DateFormat fmt = new SimpleDateFormat("yyy-MM-dd");
            Date date = fmt.parse(time);
            String dd = fmt.format(date);
            return dd + " " + "星期" + days[date.getDay()];
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }

    }


    public static String getTimeStr(String fmtStr, String time) {
        try {
            DateFormat fmt = new SimpleDateFormat(fmtStr);
            Date date = fmt.parse(time);
            String dd = fmt.format(date);
            return dd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 获取系统日期yyyy-MM-dd
     */
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date());
        return time;
    }


    /**
     * 输入长整型time
     * 返回MM月dd HH:mm
     */
    public static String getStringFormat(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd HH:mm");
        java.util.Date dt = new java.sql.Date(millis * 1000);
        return sdf.format(dt);
    }


    /**
     * 输入长整型time
     * 根据对应的转换格式进行转换
     */
    public static String getStringFormat(long millis, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        java.util.Date dt = new java.sql.Date(millis * 1000);
        return sdf.format(dt);
    }


    /**
     * 根据生日获取岁数
     *
     * @param birthday
     * @return
     */
    public static String getAgeByBirth(String birthday) {
        try {
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(birthday);
            Calendar cal = Calendar.getInstance();

            if (cal.before(birthday)) {
                return "";
            }

            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(date);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    // monthNow==monthBirth
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                } else {
                    // monthNow>monthBirth
                    age--;
                }
            }
            return age + "岁";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }


    public static long getCurrTimeLong() {
        return System.currentTimeMillis() / 1000;
    }


    public static String timestampToStr(long dateline) {
        Timestamp timestamp = new Timestamp(dateline * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
        return df.format(timestamp);
    }


    /**
     * 以友好的方式显示时间
     *
     * @return
     */
    public static String friendly_time(long dateline) {
        Date time = toDate(timestampToStr(dateline));
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.format(cal.getTime());
        String paramDate = dateFormater2.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                long timeSpace = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1);// + "分钟前";
                if (timeSpace > 2) {
                    //大于两分钟的时候，显示具体时间
                    ftime = dateFormater4.format(time);//显示具体时间
                } else {//否则显示前后
//					ftime = timeSpace + "分钟前";
                    ftime = "刚刚";
                }
            } else {
                ftime = dateFormater4.format(time);//显示具体时间
            }
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                long timeSpace = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1);// + "分钟前";
                if (timeSpace > 2) {
                    //大于两分钟的时候，显示具体时间
                    ftime = dateFormater4.format(time);//显示具体时间
                } else {//否则显示前后
//					ftime = timeSpace + "分钟前";
                    ftime = "刚刚";
                }
            } else {
                ftime = dateFormater4.format(time);//显示具体时间
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days >= 2) {
            ftime = dateFormater5.format(time);//显示具体时间
        }
        return ftime;
    }


    public final static long DIS_INTERVAL = 300;

    //判断时间的一段间隔的有效行
    public static boolean LongInterval(long current, long last) {
        return (current - last) > DIS_INTERVAL ? true : false;
    }
}
