package com.nicolasbettenburg.asttraversal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import difflib.StringUtills;

public class FindVariablesVisitor extends ASTVisitor {

	List<FindVariablesInfo> listOfVariables;
	
	public FindVariablesVisitor() {
		super();
		this.listOfVariables = new ArrayList<FindVariablesInfo>();
	}
	
	public void preVisit (ASTNode node){
		if (node instanceof VariableDeclaration) {
			/*
			if (node instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) node;
				System.out.println(svd.getName() + " :: " + svd.getType());
			}
			*/
			
			if (node instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment vdf = (VariableDeclarationFragment) node;
				if (vdf.getParent() instanceof VariableDeclarationStatement) {
					VariableDeclarationStatement vds = (VariableDeclarationStatement) vdf.getParent();
					String m = getParentMethod(vdf);
					System.out.println("[" +  m + "] " + vdf.getName() + " :: " + vds.getType());
				}
				
			}
		}
		
		
	}

	/**
	 * @return the listOfVariables
	 */
	public List<FindVariablesInfo> getListOfVariables() {
		return listOfVariables;
	}
	
	private String getParentMethod(ASTNode n) {
		
		if (n == n.getRoot()) 
			return "";
		
		if (n instanceof MethodDeclaration) {
			MethodDeclaration md = (MethodDeclaration) n;
			String params = StringUtills.join(md.parameters(), ", ");
			String res = md.getReturnType2().toString() + " " + md.getName().toString() + "(" + params + ")";
			return res;
		} else {
			return getParentMethod(n.getParent());
		}
	}
	
	
}
