package com.nykj.wxisalipaygw.util;

import com.nykj.wxisalipaygw.constants.GlobalConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Verson on 2016/5/6.
 */
public class DateUtil {
    public static String formatDate(Date date,String format) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone(GlobalConstants.DATE_TIMEZONE));
        return sdf.format(date);
    }
}
