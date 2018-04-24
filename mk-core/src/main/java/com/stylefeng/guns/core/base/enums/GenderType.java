package com.stylefeng.guns.core.base.enums;
/**
 * 性别枚举
 * @author Administrator
 *
 */
public enum GenderType {
	MAN("m","男"),
	WOMEN("f","女"),
	NONE("n","未知");
	
	String val;
    String message;

    GenderType(String val, String message) {
        this.val = val;
        this.message = message;
    }

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
	  public static String valueStrOf(String value) {
	        if (value == null) {
	            return NONE.getMessage();
	        } else {
	            for (GenderType bizType : GenderType.values()) {
	                if (bizType.getVal().equals(value)) {
	                    return bizType.getMessage();
	                }
	            }
	            return NONE.getMessage();
	        }
	    }
	  
}
