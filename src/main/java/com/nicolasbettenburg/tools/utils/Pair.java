package com.nicolasbettenburg.tools.utils;

public class Pair<T, S> {
	
	private T t;
	private S s;
	
	public Pair(T t, S s) {
		this.t = t;
		this.s = s;
	}
	
	public T getFirst() {
		return t;
	}
	
	public S getSecond() {
		return s;
	}

	public void setFirst(T t) {
		this.t = t;
	}
	
	public void setSecond(S s) {
		this.s = s;
	}
	
	@Override public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("<");
		buffer.append(t.toString());
		buffer.append(", ");
		buffer.append(s.toString());
		buffer.append(">");
		return buffer.toString();
	}
	
}
