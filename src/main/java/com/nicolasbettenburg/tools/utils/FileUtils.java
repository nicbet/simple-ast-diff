/*
 * $Id: FileUtils.java,v 1.4 2008/03/28 20:46:00 zimmerth Exp $
 * 
 * LICENSE:
 */
package com.nicolasbettenburg.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Thomas Zimmermann, zimmerth@cs.uni-sb.de
 */
public class FileUtils {
    
    /**
     * Writes a String to a file overriding the file if it does exist.
     *
     * @param fileName 
     * @param data 
     * @param encoding 
     * @throws IOException 
     */
    public static void writeStringToFile(String fileName, String data, String encoding) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        OutputStream out = new FileOutputStream(file);
        try {
            out.write(data.getBytes(encoding));
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
    
    public static void writeStringToFile(String fileName, String data) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        OutputStream out = new FileOutputStream(file);
        try {
            out.write(data.getBytes());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    
    /**
     * <p>
     * Reads the contents of a file into a String.
     * </p>
     * <p>
     * There is no readFileToString method without encoding parameter because
     * the default encoding can differ between platforms and therefore results
     * in inconsistent results.
     * </p>
     *
     * @param fileName  
     * @param encoding  
     * @return String
     * @throws IOException 
     */
    public static String readFileToString(String fileName, String encoding) throws IOException {
        StringBuffer data = new StringBuffer(4096);
        InputStreamReader input = new InputStreamReader(new FileInputStream(fileName), encoding);
        try {
            char[] buffer = new char[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                data.append(buffer, 0, n);
            }
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                // ignore
            }
        }
        return data.toString();
    }
    
    /**
     * @param directory
     */
    public static void createIfNotExists(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * 
     * @param aFile
     * @return
     */
    static public String getContents(File aFile) {
	    //...checks on aFile are elided
	    StringBuilder contents = new StringBuilder();
	    
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(aFile));
	      try {
	        String line = null; //not declared within while loop
	        /*
	        * readLine is a bit quirky :
	        * it returns the content of a line MINUS the newline.
	        * it returns null only for the END of the stream.
	        * it returns an empty String if two newlines appear in a row.
	        */
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      throw new RuntimeException("Error reading file! " + ex.getLocalizedMessage());
	    }
	    
	    return contents.toString();
	  }
    
    public static String ls() {
    	return System.getProperty("line.separator");
    }
    
}
