package com.nicolasbettenburg.treediff.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T extends Comparable> implements Comparable<Node<T>>{
	
	public T label;
	public List<Node<T>> children;
	public Node<T> parent;

	public Node(T label) {
		super();
		this.setLabel(label);
		this.children = new ArrayList<Node<T>>();
		this.parent = null;
	}
	
	public void setLabel(T label) {
		this.label = label;
	}
	
	public void addChild(Node<T> child) {
		child.setParent(this);
		this.children.add(child);
	}
	
	public void deleteChild(Node<T> child) {
		child.setParent(null);
		this.children.remove(child);
	}
	
	public void setParent(Node<T> parent) {
		this.parent = parent;
	}
	
	public Node<T> getParent() {
		return this.parent;
	}
	
	public List<Node<T>> getChildren() {
		return this.children;
	}
	
	public boolean hasParent() {
		return (this.parent == null ? false : true);
	}
	
	@Override public String toString() {
		return this.label.toString();
	}

	@Override
	public int compareTo(Node<T> o) {
		return this.label.compareTo(o.label);
	}
}
