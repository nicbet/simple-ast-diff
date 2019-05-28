package com.nicolasbettenburg.tools.diff.unified;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.nicolasbettenburg.tools.utils.Pair;
import com.nicolasbettenburg.tools.utils.ThreeTuple;

public class Delta {
	
	public static final int MINUS = -1;
	public static final int PLUS = 1;
	public static final int SAME = 0;
	public static final int ERROR = 42;
	
	// File header
	private String filename;
	private String before_line;
	private String after_line;
	
	// Hunk information
	private int before_start;
	private int before_num_lines;
	
	private int after_start;
	private int after_num_lines;

	// Content
	private List<String> lines;
	
	// Default Constructor initializes the List
	public Delta() {
		this.lines = new ArrayList<String>();
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the before_line
	 */
	public String getBefore_line() {
		return before_line;
	}

	/**
	 * @param beforeLine the before_line to set
	 */
	public void setBefore_line(String beforeLine) {
		before_line = beforeLine;
	}

	/**
	 * @return the after_line
	 */
	public String getAfter_line() {
		return after_line;
	}

	/**
	 * @param afterLine the after_line to set
	 */
	public void setAfter_line(String afterLine) {
		after_line = afterLine;
	}

	/**
	 * @return the before_start
	 */
	public int getBefore_start() {
		return before_start;
	}

	/**
	 * @param beforeStart the before_start to set
	 */
	public void setBefore_start(int beforeStart) {
		before_start = beforeStart;
	}

	/**
	 * @return the before_num_lines
	 */
	public int getBefore_num_lines() {
		return before_num_lines;
	}

	/**
	 * @param beforeNumLines the before_num_lines to set
	 */
	public void setBefore_num_lines(int beforeNumLines) {
		before_num_lines = beforeNumLines;
	}

	/**
	 * @return the after_start
	 */
	public int getAfter_start() {
		return after_start;
	}

	/**
	 * @param afterStart the after_start to set
	 */
	public void setAfter_start(int afterStart) {
		after_start = afterStart;
	}

	/**
	 * @return the after_num_lines
	 */
	public int getAfter_num_lines() {
		return after_num_lines;
	}

	/**
	 * @param afterNumLines the after_num_lines to set
	 */
	public void setAfter_num_lines(int afterNumLines) {
		after_num_lines = afterNumLines;
	}

	/**
	 * @return the lines
	 */
	public List<String> getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	
	
	public String getBefore() {
		StringBuilder buffer = new StringBuilder();
		final String line_end = System.getProperty("line.separator");
		for (String line: lines) {
			if (line.startsWith(" ") || line.startsWith("-")) {
				buffer.append(line.substring(1));
				buffer.append(line_end);
			}
		}
		return buffer.toString();
	}
	
	public String getAfter() {
		StringBuilder buffer = new StringBuilder();
		final String line_end = System.getProperty("line.separator");
		for (String line: lines) {
			if (line.startsWith(" ") || line.startsWith("+")) {
				buffer.append(line.substring(1));
				buffer.append(line_end);
			}
		}
		return buffer.toString();
	}
	
		
	public List<ThreeTuple<Integer, Integer, Boolean>> getLineMap() {
		List<ThreeTuple<Integer, Integer, Boolean>> line_map = new ArrayList<ThreeTuple<Integer,Integer, Boolean>>();
		int l = getBefore_start();
		int r = getAfter_start();
		Queue<Integer> minus = new LinkedList<Integer>();
		boolean inMinus = false;
		for (int i = 0; i < lines.size(); i++) {

			if (opForLine(i) == SAME) {
				while (! minus.isEmpty()) {
					line_map.add(new ThreeTuple<Integer, Integer, Boolean>(minus.poll(), -1, true));
				}
				inMinus = false;
				line_map.add(new ThreeTuple<Integer, Integer, Boolean>(l, r, false));
				l++;
				r++;
			}
			
			if (opForLine(i) == MINUS) {
				minus.add(l);
				l++;
				inMinus = true;
			}
			
			if (opForLine(i) == PLUS) {
				if (! minus.isEmpty()) {
					line_map.add(new ThreeTuple<Integer, Integer, Boolean>(minus.poll(), r, true));
				}
				else if (inMinus) {
					line_map.add(new ThreeTuple<Integer, Integer, Boolean>((l-1), r, true));
				} else {
					line_map.add(new ThreeTuple<Integer, Integer, Boolean>(-1, r, true));
				}
				r++;
			}
			
		}
		
		// If we still have elements in minus after all of this - probably all the lines were deleted!
		while (! minus.isEmpty()) {
			line_map.add(new ThreeTuple<Integer, Integer, Boolean>(minus.poll(), -1, true));
		}
		
		return line_map;
	}
	
	public int opForLine(int i) {
		if (i >= lines.size() || i < 0)
			return 0;
		String l = lines.get(i);
		if (l.startsWith(" "))
			return SAME;
		else if (l.startsWith("-"))
			return MINUS;
		else if (l.startsWith("+"))
			return PLUS;
		else
			return ERROR;
	}
	
	public List<Pair<Integer, Integer>> getModifiedLines() {
		List<Pair<Integer, Integer>> lines = new ArrayList<Pair<Integer, Integer>>();
		for (ThreeTuple<Integer, Integer, Boolean> mapping : getLineMap()) {
			if (mapping.getThird() && mapping.getFirst() >= 0 && mapping.getSecond() >= 0)
				lines.add(new Pair<Integer, Integer>(mapping.getFirst(), mapping.getSecond()));
		}
		return lines;
	}
	
	public List<Integer> getAddedLines() {
		List<Integer> lines = new ArrayList<Integer>();
		for (ThreeTuple<Integer, Integer, Boolean> mapping : getLineMap()) {
			if (mapping.getThird() && mapping.getFirst() == -1)
				lines.add(mapping.getSecond());
		}
		return lines;
	}
	
	public List<Integer> getDeletedLines() {
		List<Integer> lines = new ArrayList<Integer>();
		for (ThreeTuple<Integer, Integer, Boolean> mapping : getLineMap()) {
			if (mapping.getThird() && mapping.getSecond() == -1)
				lines.add(mapping.getFirst());
		}
		return lines;
	}
	
	public String getLeftFilename() {
		return before_line.trim().split("[ \\t]")[0];
	}
	
	public String getRightFilename() {
		return after_line.trim().split("[ \\t]")[0];
	}
	
}
