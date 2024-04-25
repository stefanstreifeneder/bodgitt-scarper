<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.buyer.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Connection Data Buyer</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>
You have connected to the database.
<br>
<br>
Your ID : <%= request.getParameter("ID") %>	
<br>
<br>




<%
String ip = request.getParameter("IP");
String port = request.getParameter("PORT");
session.setAttribute("IP", ip);
session.setAttribute("PORT", port);
InterfaceClient_Buyer client = null;
try{
	client = SocketConnector_Buyer.getRemote((String)session.getAttribute("IP"), 
			(String)session.getAttribute("PORT"));
}catch(Exception e){
	String msg = "CONNECTION HAS FAILED!";
	%>
	
	<jsp:forward page="/_errorPage.jsp">
			<jsp:param name="ERROR" value="conErr"/>
			<jsp:param name="CON_MSG" value="<%= msg %>"/>
		</jsp:forward>
	<%	
}
session.setAttribute("CON", client);

%>
Connection: <%= client.toString() %>
<br>
<br>
Session ID: <%= session.getId() %>
<br>
<br>

<form action="allRecordsBuyerRent.jsp" method="post">

	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<br>
	<br>
	<input type="submit" value="Buyer Main Window">
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