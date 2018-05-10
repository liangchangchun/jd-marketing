package com.mk.convention.model;

public class OrderDetailsWms {
	private String productSku; //销售sku
	private Integer qty; //数量
	private String productTitle; //产品名称
	private String productUrl; //产品url
	private String refItemId; //跟踪明细id
	private Double unitPrice; //单价
	private Double unitFinalValueFee; //单个成交费
	private Double transactionPrice; //总成交费
	
	private String warehouseSku;//仓库SKU信息
	
	public String getProductSku() {
		return productSku;
	}
	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public String getProductUrl() {
		return productUrl;
	}
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	public String getRefItemId() {
		return refItemId;
	}
	public void setRefItemId(String refItemId) {
		this.refItemId = refItemId;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getUnitFinalValueFee() {
		return unitFinalValueFee;
	}
	public void setUnitFinalValueFee(Double unitFinalValueFee) {
		this.unitFinalValueFee = unitFinalValueFee;
	}
	public Double getTransactionPrice() {
		return transactionPrice;
	}
	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	public String getWarehouseSku() {
		return warehouseSku;
	}
	public void setWarehouseSku(String warehouseSku) {
		this.warehouseSku = warehouseSku;
	}
	@Override
	public String toString() {
		return "OrderDetailsWms [productSku=" + productSku + ", qty=" + qty + ", productTitle=" + productTitle
				+ ", productUrl=" + productUrl + ", refItemId=" + refItemId + ", unitPrice=" + unitPrice
				+ ", unitFinalValueFee=" + unitFinalValueFee + ", transactionPrice=" + transactionPrice
				+ ", warehouseSku=" + warehouseSku + "]";
	}
	
	
	

}
