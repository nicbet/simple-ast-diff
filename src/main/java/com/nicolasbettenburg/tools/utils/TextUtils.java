package com.nicolasbettenburg.tools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextUtils {
	
	/**
	 * Parse a number from Object o without throwing Exception
	 * @param o the object to parse the number from
	 * @return a number if no exception occurred, -1 otherwise
	 */
	public static int parseIntX(Object o) {
        try {
            int result = Integer.parseInt((String) o);
            return result;
        } catch (Exception e) { // ignore
        }
        return -1;
    }

	/**
	 * Parse a number from Object o
	 * @param o the object to parse a number from
	 * @return a number
	 */
	public static int parseInt(Object o) {
        return Integer.parseInt((String) o);
    }

	/**
	 * Parse a date from String s
	 * @param s the String to parse the date from
	 * @return a java.sql.Timestamp object
	 */
	public static Timestamp parseDate(String s) {
        DateFormat[] dfs = {
                new SimpleDateFormat("yy-MM-dd HH:mm:ss"),
                new SimpleDateFormat("yy-MM-dd HH:mm"),
                new SimpleDateFormat("MM/dd/yy HH:mm"),
                new SimpleDateFormat("yyMMddHHmmss")
        };

        for (int i = 0; i < dfs.length; i++) {
            try {
                Date date = dfs[i].parse(s);
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                // do nothing
            }
        }
        return null;
    }
    
    /**
     * Used for filename padding
    */
    public static String padWithZeros(int number, int nzeros) {
        return String.format("%0" + nzeros + "d", number);  
   }
    
    public static String unNullify(String s) {
    	if (s != null)
    		return s;
    	else
    		return "";
    }
    
    /**
     * Convert any InputStream into a String
     */
    public static String slurp (InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
