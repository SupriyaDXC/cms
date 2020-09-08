package com.dxc.cms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class CustomerDAO {

	public Customer findByCustomerName(String name) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select * from Customer Where Cus_Name=?";
		PreparedStatement  pst = con.prepareStatement(cmd); 
		pst.setString(1, name);
		ResultSet rs = pst.executeQuery();
		Customer customer = null;
		if (rs.next()) {
			customer = new Customer();
			customer.setCusId(rs.getInt("CUS_ID"));
			customer.setCusName(rs.getString("CUS_NAME"));
			customer.setCusPassword(rs.getString("CUS_PASSWORD"));
			customer.setCusEmail(rs.getString("CUS_EMAIL"));
			customer.setCusMobile(rs.getString("CUS_MOBILE"));
			customer.setCusDob(rs.getDate("CUS_DOB"));
			customer.setCusAddress(rs.getString("CUS_ADDRESS"));
		}
		return customer;
	}
	public Customer searchByCustomerId(int custId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select * from Customer Where Cus_ID=?";
		PreparedStatement  pst = con.prepareStatement(cmd); 
		pst.setInt(1, custId);
		ResultSet rs = pst.executeQuery();
		Customer customer = null;
		if (rs.next()) {
			customer = new Customer();
			customer.setCusId(rs.getInt("CUS_ID"));
			customer.setCusName(rs.getString("CUS_NAME"));
			customer.setCusPassword(rs.getString("CUS_PASSWORD"));
			customer.setCusEmail(rs.getString("CUS_EMAIL"));
			customer.setCusMobile(rs.getString("CUS_MOBILE"));
			customer.setCusDob(rs.getDate("CUS_DOB"));
			customer.setCusAddress(rs.getString("CUS_ADDRESS"));
		}
		return customer;
	}
	
	
	public Customer[] showCustomer() throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		PreparedStatement pst=con.prepareStatement("select count(*) cnt from Customer");
		ResultSet rs=pst.executeQuery();
		rs.next(); 
		int cnt=rs.getInt("cnt"); 
		Customer[] arr=new Customer[cnt]; 
		pst=con.prepareStatement("select * from Customer"); 
		rs=pst.executeQuery();
		int i=0;
		Customer c=null;
		while(rs.next()) {
			c=new Customer();
			c.setCusId(rs.getInt("CUS_ID"));
			c.setCusName(rs.getString("CUS_NAME"));
			c.setCusPassword(rs.getString("CUS_PASSWORD"));
			c.setCusEmail(rs.getString("CUS_EMAIL"));
			c.setCusMobile(rs.getString("CUS_MOBILE"));
			c.setCusDob(rs.getDate("CUS_DOB"));
			c.setCusAddress(rs.getString("CUS_ADDRESS"));
			arr[i]=c;
			i++;
		}
		return arr;

	}
	public int authenticate(String user, String password) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select count(*) cnt from Customer where CUS_NAME=? "
				+ " AND CUS_PASSWORD=?";
		PreparedStatement  pst = con.prepareStatement(cmd);
		pst.setString(1, user);
		pst.setString(2, password);
		ResultSet rs = pst.executeQuery();
		rs.next();
		int cnt = rs.getInt("cnt");
		return cnt;
	}
}
