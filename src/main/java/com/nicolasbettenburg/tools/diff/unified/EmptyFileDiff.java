package com.nicolasbettenburg.tools.diff.unified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmptyFileDiff {
	
	
	// All with + lines
	public static String emptyFileAdd(String contents, String filename) {
		List<String> lines = new ArrayList<String>();
		lines.addAll(Arrays.asList(contents.split(System.getProperty("line.separator"))));
		int size = lines.size();
		
		StringBuilder sb = new StringBuilder();
		
		String line1 = "--- /dev/null " ;
		String line2 = "+++ " + filename ;
		String line3 = "@@ -0,0 +1," + size + " @@";
		
		sb.append(line1);
		sb.append(System.getProperty("line.separator"));
		sb.append(line2);
		sb.append(System.getProperty("line.separator"));
		sb.append(line3);
		sb.append(System.getProperty("line.separator"));
		for (String line : lines) {
			sb.append("+");
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

}
