package com.stylefeng.guns.core.aliyun;

public class AliyunModel {
	 private String accessKey;
	    private String accessSecret;
	    private String mnsAccountEndPoint;
	    private String ossBucketName;
	    private String sts;
	    
	    private String reg;
	    private String find;
	    private String bind;
	    
		public String getAccessKey() {
			return accessKey;
		}
		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}
		public String getAccessSecret() {
			return accessSecret;
		}
		public void setAccessSecret(String accessSecret) {
			this.accessSecret = accessSecret;
		}
		public String getMnsAccountEndPoint() {
			return mnsAccountEndPoint;
		}
		public void setMnsAccountEndPoint(String mnsAccountEndPoint) {
			this.mnsAccountEndPoint = mnsAccountEndPoint;
		}
		public String getOssBucketName() {
			return ossBucketName;
		}
		public void setOssBucketName(String ossBucketName) {
			this.ossBucketName = ossBucketName;
		}
		public String getSts() {
			return sts;
		}
		public void setSts(String sts) {
			this.sts = sts;
		}
		public String getReg() {
			return reg;
		}
		public void setReg(String reg) {
			this.reg = reg;
		}
		public String getFind() {
			return find;
		}
		public void setFind(String find) {
			this.find = find;
		}
		public String getBind() {
			return bind;
		}
		public void setBind(String bind) {
			this.bind = bind;
		}
}
