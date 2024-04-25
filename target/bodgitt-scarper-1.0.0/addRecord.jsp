<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.seller.*" %>

<%@ page errorPage="_errorPage.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BS Add Record</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>





This page enables you to add a new Record.





<form action="addShowNewRecord.jsp" method="post">
Name: <input type="text" name="name" size="32" value="">
<br>
City: <input type="text" name="city" size="64" value="">
<br>
Types:  <input type="text" name="types" size="64" value="">
<br>
Staff: <input type="text" name="staff" size="8" value="">
<br>
Rate:  <input type="text" name="rate" size="6" value="">
<br>
Owner:  <input type="text" name="owner" size="8" value="">
<br>

<br>
<br>
<br>
<br>
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">


	<input type="submit" value="Add Record">
</form>
<br>
<br>
<form action="allRecordsVendor.jsp" method="post">
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="submit" value="Go Back to Main Page">	
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