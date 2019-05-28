package com.nicolasbettenburg.simpleastdiff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.nicolasbettenburg.tools.utils.ThreeTuple;

public class GitSemDiffVisitor  extends ASTVisitor {
	private Map<Integer, String> lineMap;
	private Map<ASTNode, ThreeTuple<Integer, Integer, Integer>> modifiedNodes;
	//private List<Pair<ASTNode, String>> modifiedNodes;
	private CompilationUnit unit;
	
	public GitSemDiffVisitor(CompilationUnit unit, Map<Integer, String> lineMap) {
		this.lineMap = lineMap;
		this.unit = unit;
		this.modifiedNodes = new HashMap<ASTNode, ThreeTuple<Integer,Integer,Integer>>();
	}
	
	
	public void postVisit (ASTNode node){
		
		//String name = node.getClass().getName();
        
		if (node instanceof org.eclipse.jdt.core.dom.TypeDeclaration) {
			
	        int docoffset = 0;
	        	
	        int node_startLineNumber = unit.getLineNumber(node.getStartPosition() + docoffset);
	        int node_endLineNumber = unit.getLineNumber(node.getStartPosition() + node.getLength());

	        Iterator<Map.Entry<Integer, String>> iter =  lineMap.entrySet().iterator();

	        while (iter.hasNext()) {
	            Map.Entry<Integer,String> entry = iter.next();
	            Integer line = entry.getKey();
	            if (node_startLineNumber <= (line-1) && node_endLineNumber >= (line+1)) {
	            	if (!this.modifiedNodes.containsKey(node))
	            		this.modifiedNodes.put(node,new ThreeTuple<Integer, Integer, Integer>(0, 0, 0));
	            	
	            	if (lineMap.get(line).equalsIgnoreCase("[A]")) {
	            		this.modifiedNodes.get(node).setFirst(this.modifiedNodes.get(node).getFirst()+1);
	            	}
	            	else if (lineMap.get(line).equalsIgnoreCase("[D]")) {
	            		this.modifiedNodes.get(node).setSecond(this.modifiedNodes.get(node).getSecond()+1);
	            	}
	            	else if (lineMap.get(line).equalsIgnoreCase("[M]")) {
	            		this.modifiedNodes.get(node).setThird(this.modifiedNodes.get(node).getThird()+1);
	            	}
	            	
			    	iter.remove();
			    }
	        }

		} // if typedeclaration
       
	}
	
	public Map<ASTNode, ThreeTuple<Integer, Integer, Integer>> getModifiedNodes() {
		return this.modifiedNodes;
	}
	
	

}
