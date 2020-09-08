package com.dxc.cms;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;







@Path("/orders")
public class OrderDetailRest {
	
	
	@POST
	@Path("/acceptOrRejectOrder/{ordId}/{vendId}/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String acceptOrRejectOrder(@PathParam("ordId") int ordId,@PathParam("vendId") int vendId,@PathParam("status") OrderStatus status) throws SQLException {
		return new OrderDAO().acceptOrRejectOrder(ordId, vendId, status);
	}

	@POST
	@Path("/cancelOrder/{ordId}/{cusId}/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String cancelOrder(@PathParam("ordId") int ordId,@PathParam("cusId") int cusId,@PathParam("status") OrderStatus status) throws SQLException {
		return new OrderDAO().cancelOrder(ordId, cusId, status);
	}


	@POST
	@Path("/placeorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String placeOrder(OrderDetail orderdetail) throws SQLException  {
		String result = new OrderDAO().placeOrder(orderdetail);
		return result;
	}
	@GET
	@Path("/vendor/{venId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<OrderDetail> venderOrder(@PathParam("venId") int venId) throws SQLException, ParseException {
		List<OrderDetail> result = new OrderDAO().vendorOrder(venId);
		return result;
	}
	@GET
	@Path("/customerPending/{cusId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<OrderDetail> customerPending(@PathParam("cusId") int cusId) throws SQLException, ParseException {
		List<OrderDetail> result = new OrderDAO().customerPendingOrder(cusId);
		return result;
	}
	@GET
	@Path("/vendorpending/{venId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<OrderDetail> vendorPendingOrder(@PathParam("venId") int venId) throws SQLException, ParseException {
		List<OrderDetail> result = new OrderDAO().vendorPendingOrder(venId);
		return result;
	}
	
	@GET
	@Path("/customer/{cusId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<OrderDetail> customerOrder(@PathParam("cusId") int cusId) throws SQLException, ParseException {
		List<OrderDetail> result = new OrderDAO().customerOrder(cusId);
		return result;
	}

	@GET
	@Path("/{ordId}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderDetail showLeave(@PathParam("ordId") int ordId) throws SQLException {
		OrderDetail result = new OrderDAO().searchOrder(ordId);
		return result;
	}
	@Path("/approveDeny/{ordId}/{venId}/{ordStatus}/{ordComment}")
	 @Consumes(MediaType.APPLICATION_JSON)
	  @Produces(MediaType.APPLICATION_JSON)
	public String approveDeny(@PathParam("ordId")int ordId,@PathParam("venId")int venId,@PathParam("ordStatus")String ordStatus,@PathParam("ordComment")String ordComment) throws SQLException {
	    String p = new OrderDAO().approveDeny(ordId,venId,ordStatus,ordComment);
	    System.out.println(p);
	    return p;
	  }
}
