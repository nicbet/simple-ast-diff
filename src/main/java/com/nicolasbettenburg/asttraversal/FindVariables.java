package com.nicolasbettenburg.asttraversal;

import com.nicolasbettenburg.tools.utils.FileUtils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class FindVariables {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String fileContent = FileUtils.readFileToString("samples/A.java",
					"UTF8");

			// Instantiate new Eclipse AST Parser
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setResolveBindings(true);

			// Parse AST for left
			parser.setSource(fileContent.toCharArray());
			
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			
			FindVariablesVisitor visitor = new FindVariablesVisitor();
			cu.accept(visitor);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
