package com.nicolasbettenburg.simpleastdiff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nicolasbettenburg.tools.diff.unified.Delta;
import com.nicolasbettenburg.tools.diff.unified.EmptyFileDiff;
import com.nicolasbettenburg.tools.diff.unified.UnifiedDiffParser;
import com.nicolasbettenburg.tools.utils.Pair;
import com.nicolasbettenburg.tools.utils.SysUtils;
import com.nicolasbettenburg.tools.utils.ThreeTuple;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class GitSemDiff {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String repoPath = args[0];
		String filename = args[1];
		String revision = args[2];
		
		try {
			String cmdStrCur = "git show " + revision + ":" + filename;
			String cmdStrPrev = "git show " + revision + "^:" + filename;
			String cmdDiff = "git diff -u " + revision + "^.." + revision + " -- " + filename;
			
			String current = SysUtils.cmdExec(cmdStrCur, repoPath);
			String previous = SysUtils.cmdExec(cmdStrPrev, repoPath);
		
			String diff = "";

			diff = SysUtils.cmdExec(cmdDiff, repoPath);
			if (diff.startsWith("[E] fatal: bad revision ")) {
				diff = EmptyFileDiff.emptyFileAdd(current, filename);
			}
			
			Map<ASTNode, ThreeTuple<Integer, Integer, Integer>> results = calculateSemanticDiff(previous, current, diff);
			
			for (ASTNode node: results.keySet()) {
				Integer added = results.get(node).getFirst();
				Integer deleted = results.get(node).getSecond();
				Integer modified = results.get(node).getThird();
				System.out.println(revision + "," + filename + "," 
						+ prettyFiRecurse(node,0) + "," 
						+ added + "," + deleted + "," + modified);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		
		
		

	}
	
	
	
public static Map<ASTNode, ThreeTuple<Integer, Integer, Integer>> calculateSemanticDiff(String left, String right, String unifiedDiff) {
		
		//
	//System.out.println("* Creating List of Deltas...");
		
		// Prepare the unified diff
		List<Delta> deltas = UnifiedDiffParser.parseUnifiedDiffS(unifiedDiff);
		
		// Instantiate new Eclipse AST Parser
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		
		// Parse AST for left
		parser.setSource(left.toCharArray());
		CompilationUnit rootLeft = (CompilationUnit) parser.createAST(null);
		
		// Parse AST for right
		parser.setSource(right.toCharArray());
		CompilationUnit rootRight = (CompilationUnit) parser.createAST(null);
		
		Map<Integer, String> diffLineMapD = new HashMap<Integer, String>();
		Map<Integer, String> diffLineMapAM = new HashMap<Integer, String>();
		
		
		for (Delta delta : deltas) {
			for (Integer line : delta.getAddedLines()) {
				diffLineMapAM.put(line, "[A]");
			}
			for (Integer line : delta.getDeletedLines()) {
				diffLineMapD.put(line, "[D]");
			}
			for (Pair<Integer, Integer> linepair : delta.getModifiedLines()) {
				diffLineMapAM.put(linepair.getSecond(), "[M]");
			}
		
		}
		
		
		Map<ASTNode, ThreeTuple<Integer, Integer, Integer>> mergedResults = new HashMap<ASTNode, ThreeTuple<Integer,Integer,Integer>>();
		
		GitSemDiffVisitor leftVisitor = new GitSemDiffVisitor(rootLeft, diffLineMapD);
		rootLeft.accept(leftVisitor);
		
		for (ASTNode node : leftVisitor.getModifiedNodes().keySet()) {
			mergedResults.put(node, leftVisitor.getModifiedNodes().get(node)); 
		}
		
		
		GitSemDiffVisitor rightVisitor = new GitSemDiffVisitor(rootRight, diffLineMapAM);
		rootRight.accept(rightVisitor);
		
		for (ASTNode node : rightVisitor.getModifiedNodes().keySet()) {
			if (mergedResults.containsKey(node)) {
				ThreeTuple<Integer, Integer, Integer> myResults = rightVisitor.getModifiedNodes().get(node);
				mergedResults.get(node).setFirst(mergedResults.get(node).getFirst() + myResults.getFirst());
				mergedResults.get(node).setSecond(mergedResults.get(node).getSecond() + myResults.getSecond());
				mergedResults.get(node).setThird(mergedResults.get(node).getThird() + myResults.getThird());
			} else {
				mergedResults.put(node, rightVisitor.getModifiedNodes().get(node)); 
			}

		}
		
		return mergedResults;
	
	}
	
	
	public static String getPath(ASTNode node, String filename) {
		StringBuilder sb = new StringBuilder();
		ASTNode parent = node.getParent();
		if (parent != null) {
			sb.append(getPath(parent, filename));
			String p = prettyFi(node);
			if (p.length() > 0) {
				sb.append("\n\t");
				sb.append(p);
				sb.append(" (");
				sb.append (filename);
				sb.append (":");
				sb.append (node.getStartPosition());
				sb.append("-");
				sb.append(node.getStartPosition() + (node.getStartPosition() + node.getLength()));
				sb.append(")");
			}
		}
		return sb.toString();
	}
	
	public static String prettyFi(ASTNode node) {
		StringBuilder buffer = new StringBuilder();
		
			TypeDeclaration n = (TypeDeclaration) node;
			
			
			if (n.isInterface())
				buffer.append("In Interface ");
			else
				buffer.append("In Class ");
			
			ASTNode parent = n.getParent();
			if (parent instanceof CompilationUnit) {
				CompilationUnit parentx = (CompilationUnit) parent;
				buffer.append(parentx.getPackage().getName());
			}
			buffer.append(".");
			buffer.append(n.getName().toString());
			
			return buffer.toString();
		
	}
	
	public static String prettyFiRecurse(ASTNode node, Integer counter) {
		
		if (node instanceof CompilationUnit) {
			CompilationUnit parentx = (CompilationUnit) node;
			String type = counter==1?"class":"inner_class";
			return(type + "," + parentx.getPackage().getName().toString());
		} else {
			String myname = "";
			if (node instanceof TypeDeclaration)
				myname = ((TypeDeclaration) node).getName().toString();
			return(prettyFiRecurse(node.getParent(),counter+1) + "." + myname);
			
		}
	}

	
	public static String cmdExec(String cmdLine) {
	    String line;
	    String output = "";
	    try {
	        Process p = Runtime.getRuntime().exec(cmdLine);
	        BufferedReader input = new BufferedReader
	            (new InputStreamReader(p.getInputStream()));
	        while ((line = input.readLine()) != null) {
	            output += (line + '\n');
	        }
	        input.close();
	        }
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return output;
	}
	
}
