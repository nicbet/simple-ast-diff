package com.nicolasbettenburg.treediff.tree;

public class DiffLabel implements Comparable<DiffLabel>{
	
	private Object label;
	private int signature=0;
	private int weight=0;
	
	public DiffLabel(Object label) {
		super();
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public Object getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(Object label) {
		this.label = label;
	}

	/**
	 * @return the signature
	 */
	public int getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(int signature) {
		this.signature = signature;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override public String toString() {
		return label.toString() + " s=" + signature + " w=" + weight;
	}

	@Override
	public int compareTo(DiffLabel o) {
		if (this.weight > o.weight)
			return -1;
		else if (this.weight < o.weight)
			return 1;
		else
			return 0;
	}
}
