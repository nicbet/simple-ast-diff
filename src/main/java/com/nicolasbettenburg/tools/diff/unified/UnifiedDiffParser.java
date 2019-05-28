package com.nicolasbettenburg.tools.diff.unified;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nicolasbettenburg.tools.utils.FileUtils;

public class UnifiedDiffParser {
	
	public static List<Delta> parseUnifiedDiff(String filename) {
		String diff = FileUtils.getContents(new File(filename));
		return parseUnifiedDiffS(diff);
	}

	public static List<Delta> parseUnifiedDiffS(String unifiedDiff) {
		List<Delta> deltas = new ArrayList<Delta>();
		String[] lines = unifiedDiff.split("[\\n\\r]");
		
		String from_line = "";
		String to_line = "";
		String hunk_line = "";
		List<String> hunklines = new ArrayList<String>();
		boolean ready = false;
		
		for (String line : lines) {
			if (line.startsWith("---")) {
				if (ready) {
					deltas.add(makeDelta(from_line, to_line, hunk_line, hunklines));
					hunklines.clear();
				} 
				from_line = line.substring(3);
			} else if (line.startsWith("+++")) {
				to_line = line.substring(3);
				ready = false;
			} else if (line.startsWith("@@")) {
				if (ready) {
					// Create delta
					deltas.add(makeDelta(from_line, to_line, hunk_line, hunklines));
					hunklines.clear();
				} else {
					ready = true;
				}
				hunk_line = line;
			} else if (line.startsWith(" ") || line.startsWith("-") || line.startsWith("+")) {
				hunklines.add(line);
			}
		}
		// Last delta
		deltas.add(makeDelta(from_line, to_line, hunk_line, hunklines));
		
		return deltas;
	}
	
	private static Delta makeDelta(String fromLine, String toLine, String hunkLine, List<String> hunklines) {
		Delta delta = new Delta();
		
		delta.setBefore_line(fromLine);
		delta.setAfter_line(toLine);
		
		String filename = fromLine.trim().split("[ \\t]")[0];
		delta.setFilename(filename);
		
		String[] hunklineparts = hunkLine.split(" ");
		String beforepart = hunklineparts[1].substring(1);
		String afterpart = hunklineparts[2].substring(1);
		
		String[] bpparts = beforepart.split(",");
		if (bpparts.length == 1) {
			delta.setBefore_start(Integer.parseInt(bpparts[0]));
			delta.setBefore_num_lines(1);
		} else {
			delta.setBefore_start(Integer.parseInt(bpparts[0]));
			delta.setBefore_num_lines(Integer.parseInt(bpparts[1]));
		}
		
		String[] apparts = afterpart.split(",");
		if (apparts.length == 1) {
			delta.setAfter_start(Integer.parseInt(apparts[0]));
			delta.setAfter_num_lines(1);
		} else {
			delta.setAfter_start(Integer.parseInt(apparts[0]));
			delta.setAfter_num_lines(Integer.parseInt(apparts[1]));
		}
		
		delta.getLines().addAll(hunklines);
		
		return delta;
	}

}
