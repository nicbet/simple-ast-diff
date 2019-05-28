package com.nicolasbettenburg.simpleastdiff;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.nicolasbettenburg.tools.diff.unified.Delta;
import com.nicolasbettenburg.tools.diff.unified.UnifiedDiffParser;
import com.nicolasbettenburg.tools.utils.FileUtils;
import com.nicolasbettenburg.tools.utils.Pair;
import com.nicolasbettenburg.tools.utils.SysUtils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import difflib.StringUtills;


public class SemDiff {

	public static void calculateSemanticDiff(String left, String right, String unifiedDiff) {
		
		System.out.println("* Creating List of Deltas...");
		
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
		
		
		System.out.println("* Traversing Syntax Trees...");
		System.out.println("* Found the following differences");
		System.out.println("---------------------------------");
		
		for (Delta delta : deltas) {
			// Visit AST
			for (Integer line : delta.getDeletedLines()) {
				SemDiffVisitor leftVisitor = new SemDiffVisitor(rootLeft, line);
				rootLeft.accept(leftVisitor);
				System.out.print("[D] Line " + (line-1) + " : " + left.split("[\\n\\r]")[line-1].trim());
				System.out.println(getPath(leftVisitor.getModifiedNodes().get(leftVisitor.getModifiedNodes().size()-1), delta.getLeftFilename()) );
				System.out.println("");
			}
			
			// Visit AST
			for (Integer line : delta.getAddedLines()) {
				SemDiffVisitor rightVisitor = new SemDiffVisitor(rootRight, line);
				rootRight.accept(rightVisitor);
				System.out.print("[A] Line " + (line-1) + " : " + right.split("[\\n\\r]")[line-1].trim());
				System.out.println(getPath(rightVisitor.getModifiedNodes().get(rightVisitor.getModifiedNodes().size()-1), delta.getRightFilename()) );
				System.out.println("");
			}
			
			// Visit AST
			for (Pair<Integer, Integer> linepair : delta.getModifiedLines()) {
				SemDiffVisitor rightVisitor = new SemDiffVisitor(rootRight, linepair.getSecond());
				rootRight.accept(rightVisitor);
				System.out.print("[M] " + "Line " + (linepair.getSecond()-1) + " : " + right.split("[\\n\\r]")[linepair.getSecond()-1].trim());
				System.out.println(getPath(rightVisitor.getModifiedNodes().get(rightVisitor.getModifiedNodes().size()-1), delta.getRightFilename()) );
				System.out.println("");
			}
		}
	}
	
	public static void main(String[] args) {
		String left = FileUtils.getContents(new File(args[0]));
		String right = FileUtils.getContents(new File(args[1]));
		String diff = "";
		
		System.out.println("* Computing Structural Differences between " + args[0] + " and " + args[1]);
		
		try {
			diff = SysUtils.cmdExec("diff -u " + args[0] + " " + args[1]);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		calculateSemanticDiff(left, right, diff);
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
		if (node.getNodeType() == ASTNode.METHOD_DECLARATION) {
			MethodDeclaration n = (MethodDeclaration) node;
			buffer.append("In method ");
			buffer.append(n.getReturnType2().toString());
			buffer.append(" ");
			buffer.append(n.getName().toString());
			buffer.append("(");
			buffer.append(StringUtills.join(n.parameters(), ", "));
			buffer.append(")");
			return buffer.toString();
		} else if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
			TypeDeclaration n = (TypeDeclaration) node;
			if (n.isInterface())
				buffer.append("In Interface ");
			else
				buffer.append("In Class ");
			
			buffer.append(n.getName().toString());
			return buffer.toString();
		} else if (node.getNodeType() == ASTNode.BLOCK) {
			return "";
		} else {
			buffer.append("In ");
			buffer.append(node.getClass().getSimpleName());
			buffer.append(" ");
			buffer.append (node.toString().split("[\\n\\r]")[0]);
			return buffer.toString();
		}
	}

}
