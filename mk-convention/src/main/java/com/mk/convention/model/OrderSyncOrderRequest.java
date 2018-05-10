package com.mk.convention.model;

import java.util.List;

/**
 * 
 * @ClassName: OrderSyncOrderRequest
 * @Description: TODO
 * @author: guowei
 * @date: 2018年1月12日 下午5:24:33
 */
public class OrderSyncOrderRequest {
	private String actionType; //ADD：新增 EDIT：编辑
	private String orderVerify; // 1:直接审核，0:不审核
	
	private String refNoIsUnique;//1：唯一，0：不唯一。(当refNoIsUnique为1时会采用参考订单号作为wms的订单号)
	private String saleOrderCode; //订单号（创建订单时返回），更新订单时为必须
	
	private OrderWms order; //
	private List<OrderDetailsWms> orderDetails; //
	private OrderAddressWms orderAddress; //

	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getOrderVerify() {
		return orderVerify;
	}

	public void setOrderVerify(String orderVerify) {
		this.orderVerify = orderVerify;
	}

	public OrderWms getOrder() {
		return order;
	}
	public void setOrder(OrderWms order) {
		this.order = order;
	}
	public OrderAddressWms getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(OrderAddressWms orderAddress) {
		this.orderAddress = orderAddress;
	}
	public List<OrderDetailsWms> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<OrderDetailsWms> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public String getRefNoIsUnique() {
		return refNoIsUnique;
	}
	public void setRefNoIsUnique(String refNoIsUnique) {
		this.refNoIsUnique = refNoIsUnique;
	}
	public String getSaleOrderCode() {
		return saleOrderCode;
	}
	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}
	
	@Override
	public String toString() {
		return "OrderSyncOrderRequest [actionType=" + actionType + ", orderVerify=" + orderVerify + ", order=" + order
				+ ", orderDetails=" + orderDetails + ", orderAddress=" + orderAddress + ", refNoIsUnique="
				+ refNoIsUnique + ", saleOrderCode=" + saleOrderCode + "]";
	}
	
}
