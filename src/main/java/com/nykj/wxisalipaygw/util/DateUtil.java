package com.nykj.wxisalipaygw.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String DATESIMPLE = "yyyy-MM-dd";
    
    private static final String TIMESIMPLE = "yyyy-MM-dd hh:mm:ss";
    
    /**
     * 获取当前时间
     * 格式：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String getCurTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESIMPLE);
        return sdf.format(new Date());
    }
    
    /**
     * 获取当前日期
     * 格式：yyyy-MM-dd
     * @return
     */
    public static String getCurDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATESIMPLE);
        return sdf.format(new Date());
    }
}
