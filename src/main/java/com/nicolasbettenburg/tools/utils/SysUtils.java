package com.nicolasbettenburg.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SysUtils {

	
	/**
	 * Run a system command and return the resulting output to stdout as String
	 * @param cmdLine
	 * @return
	 * @throws IOException
	 */
	public static String cmdExec(String cmdLine) throws IOException {
		String line;
		StringBuilder buffer = new StringBuilder();
		Process p = Runtime.getRuntime().exec(cmdLine);
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = input.readLine()) != null) {
			buffer.append(line);
			buffer.append(System.getProperty("line.separator"));
		}
		
		input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		while ((line = input.readLine()) != null) {
			buffer.append("[E] ");
			buffer.append(line);
			buffer.append(System.getProperty("line.separator"));
		}
		
		// Need to close all automatically opened Streams to prevent IOException: too many open files
		p.getErrorStream().close();
		p.getOutputStream().close();
		p.getInputStream().close();

		return buffer.toString();
	}
	
	public static String cmdExec(String cmdLine, String pwd) throws IOException {
		String line;
		StringBuilder buffer = new StringBuilder();
		Process p = Runtime.getRuntime().exec(cmdLine, null, new File(pwd));
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = input.readLine()) != null) {
			buffer.append(line);
			buffer.append(System.getProperty("line.separator"));
		}
		
		input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		while ((line = input.readLine()) != null) {
			buffer.append("[E] ");
			buffer.append(line);
			buffer.append(System.getProperty("line.separator"));
		}
		
		// Need to close all automatically opened Streams to prevent IOException: too many open files
		p.getErrorStream().close();
		p.getOutputStream().close();
		p.getInputStream().close();

		return buffer.toString();
	}
	
	
}
