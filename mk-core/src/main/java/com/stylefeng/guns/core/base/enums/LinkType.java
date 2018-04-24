package com.stylefeng.guns.core.base.enums;
/**
 * 超链接类型
 * @author Administrator
 *
 */
public enum LinkType {
	
	LINK_0_NORMAL("0","跳转链接"),
	LINK_1_PAY_PAGE("1","跳转支付页"),
	LINK_2_SHARE("2","跳转分享"),
	LINK_3_PAY_MEAL("3","跳转支付套餐"),
	LINK_4_ID("4","身份验证"),
	LINK_5_QQ_GROUP("5","QQ群"),
	LINK_6_BOOK_LOGIN("6","小说登录");

	String val;
    String message;

    LinkType(String val, String message) {
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
    
	  public static String valueStrOf(String linkType) {
	        if (linkType == null) {
	            return LINK_0_NORMAL.getMessage();
	        } else {
	            for (GenderType bizType : GenderType.values()) {
	                if (bizType.getVal().equals(linkType)) {
	                    return bizType.getMessage();
	                }
	            }
	            return LINK_0_NORMAL.getMessage();
	        }
	    }
}
