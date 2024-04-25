<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.seller.*" %>
<%@ page errorPage="_errorPage.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BS SELLER Update</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>

This page enables you to update a Record.


<%
String recNo = null;
recNo = request.getParameter("recNo");
if(recNo == null){	
	System.out.println("rent, NULL ");	
%>
	<jsp:forward page="/_errorPage.jsp">
		<jsp:param name="ERROR" value="update"/>
	</jsp:forward>
<%	
}
InterfaceClient_Seller client = (InterfaceClient_Seller)session.getAttribute("CON");
Record r = client.getRecord(Long.parseLong(recNo));
%>

<form action="updateNewRecord.jsp" method="post">

	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="thisField" name="recNo" value="<%= r.getRecNo() %>">
	<br>
	<br>
	<br>
	Record Number: <%= r.getRecNo() %> 
	<br>
	Name: <%= r.getName() %> <input type="text" name="name" size="32" value="<%= r.getName() %>">
	<br>
	City: <%= r.getCity() %> <input type="text" name="city" size="64" value="<%= r.getCity() %>">
	<br>
	Types: <%= r.getTypesOfWork() %> <input type="text" name="types" size="64" value="<%= r.getTypesOfWork() %>">
	<br>
	Staff: <%= r.getNumberOfStaff() %> <input type="text" name="staff" size="8" value="<%= r.getNumberOfStaff() %>">
	<br>
	Rate: <%= r.getHourlyChargeRate() %> <input type="text" name="rate" size="6" value="<%= r.getHourlyChargeRate() %>">
	<br>
	Owner: <%= r.getOwner() %> <input type="text" name="owner" size="8" value="<%= r.getOwner() %>">
	<br>
	
	<br>
	<br>
	<br>
	<br>
	<input type="submit" value="Update">
</form>


<br>
<br>
<br>
<br>
Author: stefan.streifeneder@gmx.de
<br>
<br>


</body>
</html>