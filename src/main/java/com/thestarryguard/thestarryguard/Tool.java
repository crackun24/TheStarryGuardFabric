package com.thestarryguard.thestarryguard;

public class Tool {//提供静态函数的工具类
    private static final long YEAR = 31104000;//一年的秒数
    private static final long MONTH = 2592000;//一月的秒数
    private static final long DAY = 86400;//一天的秒数
    private static final long HOUR = 3600;//一小时的秒数

    private static final long MIN = 60;//一分钟的秒数
    public static String GetDateLengthDes(long date)//获取日期的长度描述
    {
        if (date / YEAR > 0)//判断是否有一年
        {
            return Long.toString(date / YEAR) + "year";
        }
        if (date / MONTH > 0)//判断是否有一月
        {
            return Long.toString(date / MONTH) + "month";
        }
        if (date / DAY > 0)//判断是否有一天
        {
            return Long.toString(date /DAY) + "day";
        }
        if (date / HOUR > 0)//判断是否有一小时
        {
            return Long.toString(date / HOUR) + "hour";
        }
        if(date / MIN > 0)//判断是否有一分钟
        {
            return Long.toString(date/MIN) + "min";
        }
        return Long.toString(date) + "sec";
    }
}
