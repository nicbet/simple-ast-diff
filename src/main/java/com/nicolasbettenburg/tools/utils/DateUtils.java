/*
 * $Id: DateUtils.java,v 1.1 2005/01/18 15:05:38 zimmerth Exp $
 * 
 * LICENSE:
 */
package com.nicolasbettenburg.tools.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Thomas Zimmermann, zimmerth@cs.uni-sb.de
 */
public class DateUtils {

    private static final DateFormat[] TSFS = {
            new SimpleDateFormat("yy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yy-MM-dd HH:mm"),
            new SimpleDateFormat("MM/dd/yy HH:mm"),
            new SimpleDateFormat("yyMMddHHmmss")
    };

    private static final DateFormat[] DFS = {
        new SimpleDateFormat("yy-MM-dd"),
        new SimpleDateFormat("yy-MM-dd"),
        new SimpleDateFormat("MM/dd/yy"),
        new SimpleDateFormat("yyMMddHH")
    };

    public static Timestamp parseDate(String s) {
        for (int i = 0; i < DFS.length; i++) {
            try {
                Date date = DFS[i].parse(s);
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                // do nothing
            }
        }
        return null;
    }

    public static Timestamp parseTimestamp(String s) {
        for (int i = 0; i < TSFS.length; i++) {
            try {
                Date date = TSFS[i].parse(s);
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                // do nothing
            }
        }
        return null;
    }
}
