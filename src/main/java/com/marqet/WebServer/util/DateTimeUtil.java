package com.marqet.WebServer.util;

import java.util.Date;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class DateTimeUtil {
    public long getNow(){
        //return second
        return new Date().getTime()/1000;
    }
}
