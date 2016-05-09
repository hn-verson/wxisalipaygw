package com.nykj.wxisalipaygw.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Verson on 2016/5/6.
 */
public class DateUtil {
    public static String formatDate(Date date,String format) throws Exception{
        return new SimpleDateFormat(format).format(date);
    }
}
