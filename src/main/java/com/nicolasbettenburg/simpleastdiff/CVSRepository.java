package com.nicolasbettenburg.simpleastdiff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CVSRepository {
	
	/**
	 * Retrieve a unified diff string for a file in a CVS Repository
	 * @param CVSROOT
	 * @param basepath
	 * @param fullqualifiedname
	 * @param r1
	 * @param r2
	 * @return
	 * @throws IOException
	 */
	public static String getDiffFor(String CVSROOT, String basepath, String fullqualifiedname, String r1, String r2) throws IOException {
		String command = "cvs -d " + CVSROOT + " diff -uN -r " + r1 + " -r " + r2 + " " + (basepath + System.getProperty("file.separator") + fullqualifiedname);
		//System.out.println("Running " + command);
		String result = cmdExec(command);
		
		return result;
	}
	
	
	/**
	 * Run a system command and return the resulting output to stdout as String
	 * @param cmdLine
	 * @return
	 * @throws IOException
	 */
	private static String cmdExec(String cmdLine) throws IOException {
		String line;
		StringBuilder buffer = new StringBuilder();
		Process p = Runtime.getRuntime().exec(cmdLine);
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = input.readLine()) != null) {
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
