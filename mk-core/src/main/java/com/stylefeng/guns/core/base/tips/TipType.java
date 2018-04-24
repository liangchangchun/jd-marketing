package com.stylefeng.guns.core.base.tips;


public enum TipType {
		SUCCESS(200, "请求成功"),
	    UPLOAD_ERROR(909, "上传失败");

	    int code;
	    String message;

	    TipType(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public static String valueOf(Integer status) {
	        if (status == null) {
	            return "";
	        } else {
	            for (TipType s : TipType.values()) {
	                if (s.getCode() == status) {
	                    return s.getMessage();
	                }
	            }
	            return "";
	        }
	    }
}
