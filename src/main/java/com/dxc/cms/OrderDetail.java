package com.dxc.cms;

import java.util.Date;
import java.util.Objects;


/**
 * Menu class used to display menu information.
 * @author hexware
 */
public class OrderDetail {
	

  private int ordId;
  private Date ordTime;

  private Double ordAmount;
 private OrderStatus ordStatus;
 
  private int qtyOrder;
 
  private int cusId;
 
  private int foodId;
 
  private int venId;
 
 
  private String walType;
 
  private String ordComments;

  
public int getOrdId() {
	return ordId;
}


public void setOrdId(int ordId) {
	this.ordId = ordId;
}


public Date getOrdTime() {
	return ordTime;
}


public void setOrdTime(Date ordTime) {
	this.ordTime = ordTime;
}


public Double getOrdAmount() {
	return ordAmount;
}


public void setOrdAmount(Double ordAmount) {
	this.ordAmount = ordAmount;
}


public OrderStatus getOrdStatus() {
	return ordStatus;
}


public void setOrdStatus(OrderStatus ordStatus) {
	this.ordStatus = ordStatus;
}


public int getQtyOrder() {
	return qtyOrder;
}


public void setQtyOrder(int qtyOrder) {
	this.qtyOrder = qtyOrder;
}


public int getCusId() {
	return cusId;
}


public void setCusId(int cusId) {
	this.cusId = cusId;
}


public int getFoodId() {
	return foodId;
}


public void setFoodId(int foodId) {
	this.foodId = foodId;
}


public int getVenId() {
	return venId;
}


public void setVenId(int venId) {
	this.venId = venId;
}


public String getWalType() {
	return walType;
}


public void setWalType(String walType) {
	this.walType = walType;
}


public String getOrdComments() {
	return ordComments;
}


public void setOrdComments(String ordComments) {
	this.ordComments = ordComments;
}


@Override
public String toString() {
	return "OrderDetail [ordId=" + ordId + ", ordTime=" + ordTime + ", ordAmount=" + ordAmount + ", ordStatus="
			+ ordStatus + ", qtyOrder=" + qtyOrder + ", cusId=" + cusId + ", foodId=" + foodId + ", venId=" + venId
			+ ", walType=" + walType + ", ordComments=" + ordComments + "]";
}


public OrderDetail(int ordId, Date ordTime, Double ordAmount, OrderStatus ordStatus, int qtyOrder, int cusId,
		int foodId, int venId, String walType, String ordComments) {
	super();
	this.ordId = ordId;
	this.ordTime = ordTime;
	this.ordAmount = ordAmount;
	this.ordStatus = ordStatus;
	this.qtyOrder = qtyOrder;
	this.cusId = cusId;
	this.foodId = foodId;
	this.venId = venId;
	this.walType = walType;
	this.ordComments = ordComments;
}


public OrderDetail() {
	
}
 
}


