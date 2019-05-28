/*
 * $Id: XMLUtils.java,v 1.4 2008/04/25 01:28:17 zimmerth Exp $
 * 
 * LICENSE:
 */
package com.nicolasbettenburg.tools.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Thomas Zimmermann, zimmerth@cs.uni-sb.de
 */
public class XMLUtils {

    
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    

    public static void removeInvalidXMLCharacters(String oldFile, String newFile) throws IOException {
        FileReader reader = null;
        FileWriter writer = null;
        try {
            reader = new FileReader(oldFile);
            writer = new FileWriter(newFile);
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];  
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                int invalid = 0;
                for (int j = 0; j < n; j++) {
                    if (invalid > 0) {
                        buffer[j - invalid] = buffer[j];
                    }
                    if (!isXMLCharacter(buffer[j])) {
                        invalid++;
                    }
                }
                writer.write(buffer, 0, n - invalid);
            }
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                // ignore
            }
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
    

    public static void encodeInvalidXMLCharacters(String oldFile, String newFile) throws IOException {
        FileReader reader = null;
        FileWriter writer = null;
        try {
            reader = new FileReader(oldFile);
            writer = new FileWriter(newFile);
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                int pos = 0;
                for (int j = 0; j < n; j++) {
                    if (!isXMLCharacter(buffer[j])) {
                        writer.write(buffer, pos, j - pos);
                        pos = j + 1;
                        String escape = XMLUtils.escapeCharater(buffer[j]);
                        writer.write(escape);
                    }
                }
                if (pos < n) {
                    writer.write(buffer, pos, n - pos);
                }
            }
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                // ignore
            }
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    
    public static String encodeInvalidXMLCharacters(String string) {
        StringBuffer result = new StringBuffer(DEFAULT_BUFFER_SIZE);
        int n = string.length();
        int pos = 0;
        for (int j = 0; j < n; j++) {
            if (!isXMLCharacter(string.charAt(j))) {
                result.append(string.substring(pos, j));
                pos = j + 1;
                String escape = XMLUtils.escapeCharater(string.charAt(j));
                result.append(escape);
            }
        }
        if (pos < n) {
            result.append(string.substring(pos, n));
        }        
        return result.toString();
    }


    public static String removeInvalidXMLCharacters(String string) {
        StringBuffer result = new StringBuffer(DEFAULT_BUFFER_SIZE);
        int n = string.length();
        int pos = 0;
        for (int j = 0; j < n; j++) {
            if (!isXMLCharacter(string.charAt(j))) {
                result.append(string.substring(pos, j));
                pos = j + 1;
            }
        }
        if (pos < n) {
            result.append(string.substring(pos, n));
        }        
        return result.toString();
    }
    
    public static boolean hasInvalidXMLCharacters(String fileName) throws IOException {
        boolean invalid = false;
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (!invalid && (-1 != (n = reader.read(buffer)))) {
                for (int j = 0; j < n; j++) {
                    if (!isXMLCharacter(buffer[j])) {
                        invalid = true;
                        break;
                    }
                }
            }
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                // ignore
            }
        }
        return invalid;
    }

    
    public static boolean isXMLCharacter(char c) {
        
            if (c == '\n') return true;
            if (c == '\r') return true;
            if (c == '\t') return true;
            
            if (c == 0xffff) return false;
            
            if (c < 0x20) return false;  if (c <= 0xD7FF) return true;
            if (c < 0xE000) return false;  if (c <= 0xFFFD) return true;
            if (c < 0x10000) return false;  if (c <= 0x10FFFF) return true;
            
            
            return false;
        }

    
    /**
     * @param ch
     * @return String
     */
    public static String escapeCharater(char ch) {
        return "&#" + ((int) ch) + ";";
    }

}
