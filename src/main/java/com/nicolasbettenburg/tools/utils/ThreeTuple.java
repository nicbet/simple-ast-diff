package com.nicolasbettenburg.tools.utils;

public class ThreeTuple<T, S, R> {
	
	private T t;
	private S s;
	private R r;
	
	public ThreeTuple(T t, S s, R r) {
		this.t = t;
		this.s = s;
		this.r = r;
	}
	
	public T getFirst() {
		return t;
	}
	
	public S getSecond() {
		return s;
	}
	
	public R getThird() {
		return r;
	}

	public void setFirst(T t) {
		this.t = t;
	}
	
	public void setSecond(S s) {
		this.s = s;
	}
	
	public void setThird(R r) {
		this.r = r;
	}
	
	@Override public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("<");
		buffer.append(t.toString());
		buffer.append(", ");
		buffer.append(s.toString());
		buffer.append(", ");
		buffer.append(r.toString());
		buffer.append(">");
		return buffer.toString();
	}

}
