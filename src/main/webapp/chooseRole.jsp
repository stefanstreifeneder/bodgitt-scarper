<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.buyer.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bodgitt and Scarper Choose Role</title>
</head>
<body>
Please connect to:
<h3>MySQL:</h3>
Port: <b>21</b>
<br>
IP: 192.168.2.104 or <b>80.151.22.160</b>
<h3>nosql:</h3>
Port: <b>3000</b>
<br>
IP: <b>192.168.2.104</b>
<br>
<br>
--------------------------------------------------------
<br>
<br>
Bodgitt and Scarper - Your broker for home improving services
<br>
<br>
Your ID : <%= request.getParameter("ID") %>	
<br>
<br>
Do you want to book a home improving contractor please register as a <b>Buyer</b>.
<br>
Are you a home improving contractor please register as a <b>Seller</b>.
<br>
<br>

---------------------------------------------------------
<%
if(session.getAttribute("CON") != null){
	System.out.println("allRecordBuyerRent, time: " + session.getCreationTime());
// 	session.invalidate();
// 	session = request.getSession();
%>
<br>
<br>
You are already registered as: <%= session.getId() %>
<br>
You are connected via: <%= session.getAttribute("CON") %>
<br>
<br>
<form action="chooseRole.jsp" method="post">
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	
	<%
		//session.removeAttribute("CON");
	 	session.invalidate();
	 	session = request.getSession();
	
	%>
	<input type="submit" value="Start new session">
</form>
---------------------------------------------------------
<%
}
%>
<br>
<br>

<br>
<form action="connectionDialog.jsp" method="post">

	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<b>Buyer</b>
	<br>
	A Buyer represents a home owner, who books the service of a home improvement
	contract(Seller). A seller offers services like paining, dry walling, plumbing or 
	something similar. 
	<br>
	<br>
	Please enter the IP and port of the server you want to be connected with:
	<br>
	<br>	
	IP: <input type="text" name="IP" size="30" value="192.168.2.104">
	<br>
	<br>
	Port: <input type="text" name="PORT" size="10" value="21">
	<br>
	<br>
	<input type="submit" value="Buyer">
</form>
	<br>
---------------------------------------------------------
	<br>
	<br>
<form action="connectionDialogVendor.jsp" method="post">
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<b>Seller</b>
	<br>
	A Seller represents a home improvement contractor, who offers services like paining, 
	dry walling, plumbing or something like
	that. 
	<br>
	<br>
	Please enter the IP and port of the server you want to be connected with:
	
	<br>
	<br>
	IP: <input type="text" name="IP" size="30" value="192.168.2.104">
	<br>
	<br>
	Port: <input type="text" name="PORT" size="10" value="21">
	<br>
	<br>
	<input type="submit" value="Seller">
	<br>
	<br>
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