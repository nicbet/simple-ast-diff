package com.nicolasbettenburg.simpleastdiff;

import java.util.Arrays;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class Diff {
	
	public static void main(String[] args) {
		
		String CVSROOT = args[0];
		String basepath = args[1];
		String fullqualifiedname = args[2];
		String r1 = args[3];
		String r2 = args[4];
		try {
			String diff = CVSRepository.getDiffFor(CVSROOT, basepath, fullqualifiedname, r1, r2);
			Patch<String> p = DiffUtils.parseUnifiedDiff(Arrays.asList(diff.split("\n")));
			for (Delta<String> d : p.getDeltas()) {
				System.out.println("Delta");
				System.out.println(d.getOriginal().getPosition() + " -> " + d.getRevised().getPosition());
				System.out.println((d.getOriginal().getPosition() + d.getOriginal().size()) + " -> " + (d.getRevised().getPosition() + d.getRevised().size()));
				System.out.println("");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
