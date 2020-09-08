package com.dxc.cms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class VendorDAO {
	public Vendor searchvendor(int venId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "Select * from vendor WHERE VEN_ID=?";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setInt(1, venId);
		ResultSet rs = pst.executeQuery();
		Vendor v = null;
		if (rs.next()) {
			v=new Vendor();
			v.setVenId(rs.getInt("VEN_ID"));
			v.setVenName(rs.getString("VEN_NAME"));
			v.setVenPassword(rs.getString("VEN_PASSWORD"));
			v.setVenEmail(rs.getString("VEN_EMAIL"));
			v.setVenMobile(rs.getString("VEN_MOBILE"));
			v.setVenAddress(rs.getString("VEN_ADDRESS"));
		}
		return v;
	}


	public Vendor[] showVendors() throws SQLException {
		Connection connection = ConnectionHelper.getConnection();
		PreparedStatement pst = connection.prepareStatement("select count(*) cnt from vendor");
		ResultSet rs = pst.executeQuery();
		rs.next();
		int cnt = rs.getInt("cnt");
		pst = connection.prepareStatement("select * from Vendor");
		rs = pst.executeQuery();
		Vendor[] vendorlist = new Vendor[cnt];
		Vendor vendor = null;
		int i=0;
		FoodStatus fs=FoodStatus.AVAILABLE;
		while(rs.next()) {
			vendor = new Vendor();
			vendor.setVenId(rs.getInt("VEN_ID"));
			vendor.setVenName(rs.getString("VEN_NAME"));
			vendor.setVenPassword(rs.getString("VEN_PASSWORD"));
			vendor.setVenEmail(rs.getString("VEN_EMAIL"));
			vendor.setVenMobile(rs.getString("VEN_MOBILE"));
			vendor.setVenAddress(rs.getString("VEN_ADDRESS"));
			
			vendorlist[i]=vendor;
			i++;
		}
		return vendorlist;

	}
	public Vendor searchByVendorName(String vendorName) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select * from Vendor where VEN_NAME=?";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setString(1, vendorName);
		ResultSet rs = pst.executeQuery();
		Vendor vendor = null;
		if (rs.next()) {
			vendor = new Vendor();
			vendor.setVenId(rs.getInt("VEN_ID"));
			vendor.setVenName(rs.getString("VEN_NAME"));
			vendor.setVenPassword(rs.getString("VEN_PASSWORD"));
			vendor.setVenEmail(rs.getString("VEN_EMAIL"));
			vendor.setVenMobile(rs.getString("VEN_MOBILE"));
			vendor.setVenAddress(rs.getString("VEN_ADDRESS"));
		}
		return vendor;
	}
	public int authenticate(String user, String password) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select count(*) cnt from Vendor where VEN_NAME=? "
				+ " AND VEN_PASSWORD=?";
		PreparedStatement  pst = con.prepareStatement(cmd);
		pst.setString(1, user);
		pst.setString(2, password);
		ResultSet rs = pst.executeQuery();
		rs.next();
		int cnt = rs.getInt("cnt");
		return cnt;
	}

}
