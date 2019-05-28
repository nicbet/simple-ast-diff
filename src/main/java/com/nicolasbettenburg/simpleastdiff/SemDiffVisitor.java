package com.nicolasbettenburg.simpleastdiff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class SemDiffVisitor  extends ASTVisitor {
	private int line;
	private List<ASTNode> modifiedNodes;
	private CompilationUnit unit;
	
	public SemDiffVisitor(CompilationUnit unit, int line) {
		this.line = line;
		this.unit = unit;
		this.modifiedNodes = new ArrayList<ASTNode>();
	}
	
	public void preVisit (ASTNode node){
		
		String name = node.getClass().getName();
        
        int docoffset = 0;
        if (node instanceof MethodDeclaration) {
        	MethodDeclaration nn = (MethodDeclaration) node;
        	Javadoc doc = nn.getJavadoc();
        	if (doc != null)
        		docoffset = doc.getLength()+1;
        	
        }

        int node_startLineNumber = unit.getLineNumber(node.getStartPosition() + docoffset);
        int node_endLineNumber = unit.getLineNumber(node.getStartPosition() + node.getLength());
        
        if (node_startLineNumber <= (line-1) && node_endLineNumber >= (line+1)) {
        	this.modifiedNodes.add(node);
        }
       
	}
	
	public List<ASTNode> getModifiedNodes() {
		return this.modifiedNodes;
	}
	
	

}
