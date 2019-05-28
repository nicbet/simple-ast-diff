package com.nicolasbettenburg.treediff.test;

import java.util.List;

import com.nicolasbettenburg.tools.diff.unified.Delta;
import com.nicolasbettenburg.tools.diff.unified.UnifiedDiffParser;
import com.nicolasbettenburg.tools.utils.Pair;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Delta> deltas = UnifiedDiffParser.parseUnifiedDiff("samples/sample4.patch");
		
		for (Delta delta : deltas ) {
			System.out.println(delta.getFilename() + " " + delta.getBefore_start());
			System.out.println("CHANGED");
			for (Pair<Integer, Integer> l : delta.getModifiedLines())
				System.out.println(l);
			
			System.out.println("DELETED");
			for (Integer l : delta.getDeletedLines())
				System.out.println(l);
			
			System.out.println("ADDED");
			for (Integer l : delta.getAddedLines())
				System.out.println(l);
		}
		
		
	}

}
