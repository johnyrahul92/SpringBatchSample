package com.springbatch.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BatchUtil {

    public static String getDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMHHmmssSSS");
        return simpleDateFormat.format(date);
    }
}
