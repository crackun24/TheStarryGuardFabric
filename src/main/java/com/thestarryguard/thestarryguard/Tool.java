package com.thestarryguard.thestarryguard;

public class Tool {//提供静态函数的工具类
    private static final float YEAR = 31104000;//一年的秒数
    private static final float MONTH = 2592000;//一月的秒数
    private static final float DAY = 86400;//一天的秒数
    private static final float HOUR = 3600;//一小时的秒数

    private static final float MIN = 60;//一分钟的秒数
    public static String GetDateLengthDes(float date)//获取日期的长度描述
    {
        if (date / YEAR >= 1)//判断是否有一年
        {
            return String.format("%.1f",date / YEAR) + "year";
        }
        if (date / MONTH >= 1)//判断是否有一月
        {
            return String.format("%.1f",date / MONTH) + "month";
        }
        if (date / DAY >= 1)//判断是否有一天
        {
            return String.format("%.1f",date / DAY) + "day";
        }
        if (date / HOUR >= 1)//判断是否有一小时
        {
            return String.format("%.1f",date / HOUR) + "hour";
        }
        if(date / MIN >= 1)//判断是否有一分钟
        {
            return String.format("%.1f",date / MIN) + "min";
        }

        return String.format("%.1f",date) + "sec";
    }
}
