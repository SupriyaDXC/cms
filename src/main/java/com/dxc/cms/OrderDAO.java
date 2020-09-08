package com.dxc.cms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;








public class OrderDAO {
	
	public String acceptOrRejectOrder(int ordId,int vendId,OrderStatus status) throws SQLException {
		Connection con=ConnectionHelper.getConnection();
		String result="";
		String cmd="update order_detail set ord_status=? where order_id=?";
		PreparedStatement pst=con.prepareStatement(cmd);
		String s=String.valueOf(status);
		pst.setString(1, s);
		pst.setInt(2, ordId);
		pst.executeUpdate();
		if(status.equals(OrderStatus.ACCEPTED)) {
			result="Order Accepted";
		}else if(status.equals(OrderStatus.REJECTED)) {
			cmd="select cus_id, Wal_Type, ORD_AMOUNT from order_detail where order_id=?";
			pst=con.prepareStatement(cmd);
			pst.setInt(1, ordId);
			ResultSet rs=pst.executeQuery();
			rs.next();
			int cusId=rs.getInt("cus_id");
			WalType walType=WalType.valueOf(rs.getString("Wal_Type"));
			int ordAmt=rs.getInt("ORD_AMOUNT");
			new WalletDAO().updateWalletOnCancle(cusId, walType, ordAmt);
			result="Order Rejected";
		}
		return result;
	}

	 public OrderDetail[] showOrder() throws SQLException {
			Connection con1 = ConnectionHelper.getConnection();
			PreparedStatement pst=con1.prepareStatement("select count(*) cnt from Orders");
			ResultSet rs=pst.executeQuery();
			rs.next(); 
			int cnt=rs.getInt("cnt"); 
			OrderDetail[] arr=new OrderDetail[cnt]; 
			pst=con1.prepareStatement("select * from Orders"); 
			rs=pst.executeQuery();
			int i=0;
			OrderDetail o=null;
			while(rs.next()) {
				o=new OrderDetail();
				o.setCusId(rs.getInt("CUS_ID"));
				o.setOrdComments(rs.getString("ORD_COMMENTS"));
				o.setFoodId(rs.getInt("FOOD_ID"));
				o.setOrdId(rs.getInt("ORDER_ID"));
			
				o.setOrdTime(rs.getDate("ORD_TIME"));
				o.setVenId(rs.getInt("VEN_ID"));
				o.setWalType(rs.getString("Wal_Type"));
				o.setQtyOrder(rs.getInt("QTY_ORDER"));
				OrderStatus ordStatus = OrderStatus.valueOf(rs.getString("ORD_STATUS"));
				
				o.setOrdStatus(ordStatus);
				o.setOrdAmount(rs.getDouble("ORD_AMOUNT"));
				arr[i]=o;
				i++;
			}
			return arr;
		}	
			
		
 

	public OrderDetail searchOrder(int ordId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "Select * from order_detail WHERE ORDER_ID=?";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setInt(1, ordId);
		ResultSet rs = pst.executeQuery();
		OrderDetail order = null;
		if (rs.next()) {
			order = new OrderDetail();
			order.setOrdId(rs.getInt("ORDER_ID"));
			order.setCusId(rs.getInt("CUS_ID"));
			order.setVenId(rs.getInt("VEN_ID"));
			order.setWalType(rs.getString("Wal_Type"));
		    
		
			order.setQtyOrder(rs.getInt("QTY_ORDER"));
			order.setOrdAmount(rs.getDouble("ORD_AMOUNT"));
			OrderStatus oS = OrderStatus.valueOf(rs.getString("ORD_STATUS"));
			order.setOrdStatus(oS);
			order.setOrdComments(rs.getString("ORD_COMMENTS"));
		}
		return order;
	}
	public String cancelOrder(int ordId,int cusId,OrderStatus status) throws SQLException {
		Connection con=ConnectionHelper.getConnection();
		String result="";
		String cmd="update order_detail set ord_status=? where order_id=?";
		PreparedStatement pst=con.prepareStatement(cmd);
		String s=String.valueOf(status);
		pst.setString(1, s);
		pst.setInt(2, ordId);
		pst.executeUpdate();
		cmd="select Wal_Type, ORD_AMOUNT from order_detail where order_id=?";
		pst=con.prepareStatement(cmd);
		pst.setInt(1, ordId);
		ResultSet rs=pst.executeQuery();
		rs.next();
		WalType walType=WalType.valueOf(rs.getString("Wal_Type"));
		int ordAmt=rs.getInt("ORD_AMOUNT");
		new WalletDAO().updateWalletOnCancle(cusId, walType, ordAmt);
		result="Order Cancelled";
		return result;
	}

	
	public <Wallet> String approveDeny(int ordId, int venId, String ordStatus, String ordComments) throws SQLException {
		 OrderDetail order = searchOrder(ordId);
		 Vendor vendor = new VendorDAO().searchvendor(venId);
		 Connection con = ConnectionHelper.getConnection();

		 if (vendor.getVenId()==order.getVenId()) {

		 String cmd = "SELECT WAL_AMOUNT FROM WALLET WHERE  CUS_ID=? AND Wal_Type=? " ;
		 PreparedStatement pst = con.prepareStatement(cmd);
		 pst.setInt(1, order.getCusId());
		 pst.setString(2, order.getWalType());
		 ResultSet rs = pst.executeQuery();
		 rs.next();
		 double walAmount = rs.getDouble("WAL_AMOUNT");

		 if (ordStatus.toUpperCase().equals("YES") && order.getOrdAmount() <= walAmount) {
		 OrderStatus o = OrderStatus.ACCEPTED;
		 cmd = "Update Order_detail SET ORD_STATUS=?, ORD_COMMENTS=? WHERE ORDER_ID=?";
		 pst = con.prepareStatement(cmd);
		 pst.setString(1, o.toString());
		 pst.setString(2, ordComments);
		 pst.setInt(3, ordId);
		 pst.executeUpdate();

		 double newAmount = walAmount - order.getOrdAmount();
		 WalType ws = WalType.valueOf(order.getWalType());
		 cmd = "Update WALLET set WAL_AMOUNT=? WHERE Wal_Type=? AND CUS_ID=?";
		 pst = con.prepareStatement(cmd);
		 pst.setDouble(1,newAmount);
		 pst.setString(2, ws.toString());
		 pst.setInt(3, order.getCusId());
		 pst.executeUpdate();

		 return "ORDER ACCEPTED...";
		 } else {
			 OrderStatus o = OrderStatus.REJECTED;
		 cmd = "Update Order_detail SET ORD_STATUS=?, ORD_COMMENTS=? WHERE ORDER_ID=?";
		 pst = con.prepareStatement(cmd);
		 pst.setString(1, o.toString());
		 pst.setString(2, ordComments);
		 pst.setInt(3, ordId);
		 pst.executeUpdate();
		 
		 return "ORDER RECEJTED...";
		 }
		 } else {
		 return "Unauthorized Vendor...";
		 }
		 
	 }	

	
	public List<OrderDetail> vendorPendingOrder(int venId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "Select * from Order_detail  WHERE VEN_ID=? AND ORD_STATUS='PENDING'";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setInt(1, venId);
		ResultSet rs = pst.executeQuery();
		List<OrderDetail> arr = new ArrayList<OrderDetail>();
		OrderDetail orderDetail = null;
		while(rs.next()) {
			orderDetail = new OrderDetail();
			orderDetail.setCusId(rs.getInt("CUS_ID"));
			orderDetail.setOrdComments(rs.getString("ORD_COMMENTS"));
			orderDetail.setFoodId(rs.getInt("FOOD_ID"));
			orderDetail.setOrdId(rs.getInt("ORDER_ID"));
		
			orderDetail.setOrdTime(rs.getDate("ORD_TIME"));
			orderDetail.setVenId(rs.getInt("VEN_ID"));
			orderDetail.setWalType(rs.getString("Wal_Type"));
			orderDetail.setQtyOrder(rs.getInt("QTY_ORDER"));
			OrderStatus ordStatus = OrderStatus.valueOf(rs.getString("ORD_STATUS"));
			
			orderDetail.setOrdStatus(ordStatus);
			orderDetail.setOrdAmount(rs.getDouble("ORD_AMOUNT"));
			
			arr.add(orderDetail);
		
		}
		return arr;
		
	}
	public List<OrderDetail> customerPendingOrder(int cusId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
	String	cmd = "select * from Order_detail where CUS_ID=? AND ORD_STATUS='PENDING'";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setInt(1, cusId);
		ResultSet rs = pst.executeQuery();
		List<OrderDetail> arr = new ArrayList<OrderDetail>();
		OrderDetail orderDetail = null;
		while(rs.next()) {
			orderDetail = new OrderDetail();
			orderDetail.setCusId(rs.getInt("CUS_ID"));
			orderDetail.setOrdComments(rs.getString("ORD_COMMENTS"));
			orderDetail.setFoodId(rs.getInt("FOOD_ID"));
			orderDetail.setOrdId(rs.getInt("ORDER_ID"));
		
			orderDetail.setOrdTime(rs.getDate("ORD_TIME"));
			orderDetail.setVenId(rs.getInt("VEN_ID"));
			orderDetail.setWalType(rs.getString("Wal_Type"));
			orderDetail.setQtyOrder(rs.getInt("QTY_ORDER"));
			OrderStatus ordStatus = OrderStatus.valueOf(rs.getString("ORD_STATUS"));
			
			orderDetail.setOrdStatus(ordStatus);
			orderDetail.setOrdAmount(rs.getDouble("ORD_AMOUNT"));
			
			arr.add(orderDetail);
		
		}
		return arr;
		
	}
	public List<OrderDetail> customerOrder(int cusId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "Select * from Order_detail  WHERE CUS_ID=?";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setInt(1, cusId);
		ResultSet rs = pst.executeQuery();
		List<OrderDetail> arr = new ArrayList<OrderDetail>();
		OrderDetail orderDetail = null;
		while(rs.next()) {
			orderDetail = new OrderDetail();
			orderDetail.setCusId(rs.getInt("CUS_ID"));
			orderDetail.setOrdComments(rs.getString("ORD_COMMENTS"));
			orderDetail.setFoodId(rs.getInt("FOOD_ID"));
			orderDetail.setOrdId(rs.getInt("ORDER_ID"));
		
			orderDetail.setOrdTime(rs.getDate("ORD_TIME"));
			orderDetail.setVenId(rs.getInt("VEN_ID"));
			orderDetail.setWalType(rs.getString("Wal_Type"));
			orderDetail.setQtyOrder(rs.getInt("QTY_ORDER"));
			OrderStatus ordStatus = OrderStatus.valueOf(rs.getString("ORD_STATUS"));
			
			orderDetail.setOrdStatus(ordStatus);
			orderDetail.setOrdAmount(rs.getDouble("ORD_AMOUNT"));
			
			arr.add(orderDetail);
		
		}
		return arr;
		
	}

	
	public List<OrderDetail> vendorOrder(int venId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "Select * from Order_detail  WHERE VEN_ID=?";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setInt(1, venId);
		ResultSet rs = pst.executeQuery();
		List<OrderDetail> arr = new ArrayList<OrderDetail>();
		OrderDetail orderDetail = null;
		while(rs.next()) {
			orderDetail = new OrderDetail();
			orderDetail.setCusId(rs.getInt("CUS_ID"));
			orderDetail.setOrdComments(rs.getString("ORD_COMMENTS"));
			orderDetail.setFoodId(rs.getInt("FOOD_ID"));
			orderDetail.setOrdId(rs.getInt("ORDER_ID"));
		
			orderDetail.setOrdTime(rs.getDate("ORD_TIME"));
			orderDetail.setVenId(rs.getInt("VEN_ID"));
			orderDetail.setWalType(rs.getString("Wal_Type"));
			orderDetail.setQtyOrder(rs.getInt("QTY_ORDER"));
			OrderStatus ordStatus = OrderStatus.valueOf(rs.getString("ORD_STATUS"));
			
			orderDetail.setOrdStatus(ordStatus);
			orderDetail.setOrdAmount(rs.getDouble("ORD_AMOUNT"));
			
			arr.add(orderDetail);
		
		}
		return arr;
		
	}
	  public String placeOrder(OrderDetail orderdetail) throws SQLException {
		  	Menu menu = new MenuDAO().searchByMenuId(orderdetail.getFoodId());
		  	Customer customer = new CustomerDAO().searchByCustomerId(orderdetail.getCusId());
		    //System.out.println(orderdetail.getFoodId());
		    // System.out.println(menu);
		    Wallet wallet = new WalletDAO().showByWalltType(orderdetail.getCusId(), orderdetail.getWalType());
		    System.out.println("Wallet Amount " + wallet.getWalAmount());
		    double walAmount = wallet.getWalAmount();
		    double price = menu.getFoodPrice();
		    Date today = new Date();
		    System.out.println(orderdetail.getOrdTime());
		    long diffTime = orderdetail.getOrdTime().getTime() - today.getTime();
		    long diffDays = diffTime / (60 * 60 * 1000 * 24);
		    //System.out.println("Diff Time " + diffDays);
		    double totalAmount = price * orderdetail.getQtyOrder();
		    if (walAmount < totalAmount) {
		      return "Insufficient Funds...";
		    }  else if (diffDays < 0)  {
		      return "Order Cannot placed yesterday...";
		    }   else {
		      double diff = walAmount - totalAmount;
		      System.out.println("Price is  " + menu.getFoodPrice());
		      orderdetail.setOrdStatus(OrderStatus.PENDING);
		      orderdetail.setOrdAmount(totalAmount);
		      java.sql.Date sqlDate = new java.sql.Date(orderdetail.getOrdTime().getTime());
		      String cmd = "Insert into order_detail(CUS_ID, FOOD_ID, VEN_ID, Wal_Type,"
		      		+ "QTY_ORDER, ORD_STATUS, ORD_AMOUNT,ORD_COMMENTS, ORD_TIME) VALUES(?, ?, ?, ?, ?,?,?,?,?)";
		      Connection con = ConnectionHelper.getConnection();
		      PreparedStatement pst = con.prepareStatement(cmd);
		      pst.setInt(1, orderdetail.getCusId());
		    
		      pst.setInt(2, orderdetail.getFoodId());
		      pst.setInt(3, orderdetail.getVenId());
		      pst.setString(4, orderdetail.getWalType());
		      pst.setInt(5, orderdetail.getQtyOrder());
		      pst.setString(6, orderdetail.getOrdStatus().toString());
		      pst.setDouble(7, orderdetail.getOrdAmount());
		      pst.setString(8, orderdetail.getOrdComments());
		      pst.setDate(9, sqlDate);
		      pst.executeUpdate();
		      cmd = "Update Wallet SET WAL_AMOUNT=? WHERE WAL_TYPE=? AND CUS_ID=?";
		      pst = con.prepareStatement(cmd);
		      pst.setDouble(1, diff);
		      pst.setString(2, orderdetail.getWalType());
		      pst.setInt(3, orderdetail.getCusId());
		      pst.executeUpdate();
		      return "Order Placed Successfully...";
		    }
		  }

}
