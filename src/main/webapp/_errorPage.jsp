<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page isErrorPage="true" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error Page</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>

<%

String err = request.getParameter("ERROR");
System.out.println("_errorPage, error: " + request.getParameter("ERROR"));
String goTo = null;
if(err.equals("rent")){
	goTo = "allRecordsBuyerRent.jsp";
}else if(err.equals("release")){
	goTo = "allRecordsRelease.jsp";
}else if(err.equals("update")){
	goTo = "allRecordsVendor.jsp";
}else if(err.equals("delete")){
	goTo = "allRecordsDelete.jsp";
}else if(err.equals("updateNewRec")){
	goTo = "allRecordsVendor.jsp";
}else if(err.equals("addNewRec")){
	goTo = "addRecord.jsp";
}else if(err.equals("rentRent")){
	goTo = "allRecordsBuyerRent.jsp";
}else if(err.equals("deleteErr")){
	goTo = "allRecordsDelete.jsp";
}else if(err.equals("releaseREL")){
	goTo = "allRecordsRelease.jsp";
}else if(err.equals("conErr")){
	goTo = "start.jsp";
}


%>
This is the Error Page.
<br>
<br>
The following problem has occurred:
<br>
<br>
<%if(err.equals("rent")){%>

You have missed to select an item to rent.
<%
}else if(err.equals("release")){%>

You have missed to select an item to release.
<%
}else if(err.equals("update")){
%>

You have missed to select an item to update a service.

<%
}else if(err.equals("delete")){
%>

You have missed to select an item to delete a service.

<%
}else if(err.equals("updateNewRec")){	
%>

Update operation fails: <b><%= request.getParameter("UP_MSG") %></b>

<%
}else if(err.equals("addNewRec")){
%>

Add operation fails: <b><%= request.getParameter("ADD_MSG") %></b>

<%
}else if(err.equals("rentRent")){
%>

Rent operation fails: <b><%= request.getParameter("RENT_MSG") %></b>

<%
}else if(err.equals("deleteErr")){
	System.out.println("_errorPage, msg: " + request.getParameter("DEL_MSG"));
	
%>

Delete operation fails: <b><%= request.getParameter("DEL_MSG") %></b>

<%
}else if(err.equals("releaseREL")){
	System.out.println("_errorPage, msg: " + request.getParameter("REL_MSG"));
	
%>

Rent operation fails: <b><%= request.getParameter("REL_MSG") %></b>

<%
}else if(err.equals("conErr")){
	System.out.println("_errorPage, msg: " + request.getParameter("CON_MSG"));
	
%>

Rent operation fails: <b><%= request.getParameter("CON_MSG") %></b>

<%
}
%>




<br>
<br>

<form action="<%= goTo %>" method="post">

	<input type="hidden" id="thisAllRecordsBuyerRent" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	<input type="submit" value="Go Back">
</form>
<br>
<br>
<br>
<br>
<br>
Author: stefan.streifeneder@gmx.de
<br>
<br>
</body>
</html>