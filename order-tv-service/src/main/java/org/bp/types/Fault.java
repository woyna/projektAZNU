package org.bp.types;

public class Fault extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int code;
    protected String text;
    
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
