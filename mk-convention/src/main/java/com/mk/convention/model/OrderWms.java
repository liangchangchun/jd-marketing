package com.mk.convention.model;
 
import java.util.List;

public class OrderWms {

	private String platform; //平台
	private String orderType; //订单类型
	private String refNo; //参考单号
	private String companyCode; //公司代码
	private String userAccount; //账号
	private String currency; //币种
	private String buyerId; //买家ID
	private String buyerName; //买家名称
	private String warehouseId;
	private String shippingMethod; //平台运输方式 
	private String shippingMethodPlatform; //平台运输方式
	private String datePaidPlatform; //付款时间
	private Double shipFee; //运费
	private Double platformFee; //手续费
	private Double finalValueFee; //交易费
	private String buyerMail; //买家邮箱
	private String site; //订单站点
	private String orderDesc; //买家留言
	
	private List<OrderDetailsWms> orderDetails;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getShippingMethodPlatform() {
		return shippingMethodPlatform;
	}
	public void setShippingMethodPlatform(String shippingMethodPlatform) {
		this.shippingMethodPlatform = shippingMethodPlatform;
	}
	public String getDatePaidPlatform() {
		return datePaidPlatform;
	}
	public void setDatePaidPlatform(String datePaidPlatform) {
		this.datePaidPlatform = datePaidPlatform;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getShipFee() {
		return shipFee;
	}
	public void setShipFee(Double shipFee) {
		this.shipFee = shipFee;
	}
	public Double getPlatformFee() {
		return platformFee;
	}
	public void setPlatformFee(Double platformFee) {
		this.platformFee = platformFee;
	}
	public Double getFinalValueFee() {
		return finalValueFee;
	}
	public void setFinalValueFee(Double finalValueFee) {
		this.finalValueFee = finalValueFee;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getBuyerMail() {
		return buyerMail;
	}
	public void setBuyerMail(String buyerMail) {
		this.buyerMail = buyerMail;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public List<OrderDetailsWms> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<OrderDetailsWms> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getShippingMethod() {
		return shippingMethod;
	}
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	 
}
