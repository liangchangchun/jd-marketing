package com.stylefeng.guns.game;


public enum MachineStatus {
	MACHINE_FREE(0, "空闲中"),
	MACHINE_ING(1, "游戏中"),
	MACHINE_REPAIR(2, "维修中"),
	MACHINE_NOT_ONLINE(3, "未上线");
	
	  private final int code;

	    private final String message;

	    MachineStatus(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
	    

	    public static String valueOf(Integer status) {
	        if (status == null) {
	            return "";
	        } else {
	            for (MachineStatus s : MachineStatus.values()) {
	                if (s.getCode() == status) {
	                    return s.getMessage();
	                }
	            }
	            return "";
	        }
	    }
}
