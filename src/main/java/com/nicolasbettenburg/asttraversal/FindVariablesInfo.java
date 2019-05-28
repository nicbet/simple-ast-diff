package com.nicolasbettenburg.asttraversal;

public class FindVariablesInfo {

	String classname;
	String methodname;
	String variablename;
	String type;
	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}
	/**
	 * @param classname the classname to set
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}
	/**
	 * @return the methodname
	 */
	public String getMethodname() {
		return methodname;
	}
	/**
	 * @param methodname the methodname to set
	 */
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	/**
	 * @return the variablename
	 */
	public String getVariablename() {
		return variablename;
	}
	/**
	 * @param variablename the variablename to set
	 */
	public void setVariablename(String variablename) {
		this.variablename = variablename;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @param classname
	 * @param methodname
	 * @param variablename
	 * @param type
	 */
	public FindVariablesInfo(String classname, String methodname,
			String variablename, String type) {
		super();
		this.classname = classname;
		this.methodname = methodname;
		this.variablename = variablename;
		this.type = type;
	}
	
	
	
}
