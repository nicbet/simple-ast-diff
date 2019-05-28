/*
 * $Id: ArrayUtils.java,v 1.5 2007/07/18 18:50:28 zimmerth Exp $
 * 
 * LICENSE:
 */
package com.nicolasbettenburg.tools.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Thomas Zimmermann, zimmerth@cs.uni-sb.de
 */
public class ArrayUtils {

	/**
	 * @param array
	 * @param delim
	 * @return String
	 */
	public static String join(int[] array, String delim) {

		StringBuffer buf = new StringBuffer();
		
		// Append the first n-1 entries. If there is only 1, nothing is done.
		for (int pos = 0; pos < array.length - 1; pos++) {
			buf.append(array[pos]);
			buf.append(delim);
		}

		// Append the last entry without a delimiter.
		if (array.length > 0) {
			buf.append(array[array.length - 1]);
		}
		
		return buf.toString();
	}
	
	
	/**
	 * @param array
	 * @param delim
	 * @return String
	 */
	public static String join(double[] array, String delim) {

		StringBuffer buf = new StringBuffer();
		
		// Append the first n-1 entries. If there is only 1, nothing is done.
		for (int pos = 0; pos < array.length - 1; pos++) {
			buf.append(array[pos]);
			buf.append(delim);
		}

		// Append the last entry without a delimiter.
		if (array.length > 0) {
			buf.append(array[array.length - 1]);
		}
		
		return buf.toString();
	}


	/**
	 * @param array
	 * @param delim
	 * @return String
	 */
	public static String join(Object[] array, String delim) {

		StringBuffer buf = new StringBuffer();
		
		// Append the first n-1 entries. If there is only 1, nothing is done.
		for (int pos = 0; pos < array.length - 1; pos++) {
			if (array[pos] != null) {
				buf.append(array[pos].toString());
				buf.append(delim);
			}
		}

		// Append the last entry without a delimiter.
		if (array.length > 0) {
			if (array[array.length - 1] != null) {
				buf.append(array[array.length - 1]);
			}
		}
		
		return buf.toString();
	}
	

	/**
	 * @param array
	 * @param outerDelim
	 * @param innerDelim
	 * @return String
	 */
	public static String join(Object[][] array, String outerDelim, String innerDelim) {

		StringBuffer buf = new StringBuffer();
		
		// Append the first n-1 entries. If there is only 1, nothing is done.
		for (int pos = 0; pos < array.length - 1; pos++) {
			if (array[pos] != null) {
				buf.append(ArrayUtils.join(array[pos], innerDelim));
				buf.append(outerDelim);
			}
		}

		// Append the last entry without a delimiter.
		if (array.length > 0) {
			if (array[array.length - 1] != null) {
				buf.append(ArrayUtils.join(array[array.length - 1], innerDelim));
			}
		}
		
		return buf.toString();
	}

	/**
	 * 
	 * @param s
	 * @param regex
	 * @return int[]
	 */
	public static int[] splitInt(String s, String regex) {
		
		String[] sArray = s.split(regex);
		
		int[] result = new int[sArray.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(sArray[i]);
		}
		
		return result;
	}


	/**
	 * 
	 * @param s
	 * @param regex
	 * @return double[]
	 */
	public static double[] splitDouble(String s, String regex) {
		
		String[] sArray = s.split(regex);
		
		double[] result = new double[sArray.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Double.parseDouble(sArray[i]);
		}
		
		return result;
	}


	/**
	 * 
	 * @param collection
	 * @return int[]
	 */
	@SuppressWarnings("rawtypes")
	public static int[] parseIntegerCollection(Collection collection)  {
		
		int[] result = new int[collection.size()];
		int i = 0;
		for (Iterator iter = collection.iterator(); iter.hasNext(); i++) {
			Integer integer = (Integer) iter.next();
			result[i] = integer.intValue();
		}
		return result;
	}


    /**
     * @param array
     * @param maxSize
     * @return int[][]
     */
    public static int[][] splitArray(int[] array, int maxSize) {
    	if (maxSize > array.length) return new int[][] { array } ;
        int lastSize = array.length % maxSize;
        int oneMore =  lastSize > 0 ? 1 : 0;
        int numberOfArrays = array.length / maxSize + oneMore;
        int[][] result = new int[numberOfArrays][];
        for (int i = 0; i < numberOfArrays - oneMore; i++) {
            result[i] = new int[maxSize];
            System.arraycopy(array, i * maxSize, result[i], 0, maxSize);
        }
        if (oneMore > 0) {
            int last = numberOfArrays - 1;
            result[last] = new int[lastSize];
            System.arraycopy(array, last * maxSize, result[last], 0, lastSize);
        }
        return result;
    }

}
