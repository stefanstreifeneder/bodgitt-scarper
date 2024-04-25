<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.buyer.*" %>
<%@ page errorPage="_errorPage.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cancellation of a Service</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>
Thank for using Bodgitt and Scarper introduced by Oracle.
<br>
<br>
Your ID: <%= request.getParameter("ID") %>
<br>
<br>
Date: <%= new Date() %>

<%   
	String userIpAddress = null;
	try{		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// Proxy
		userIpAddress = httpServletRequest.getHeader("X-Forwarded-For");
		if(userIpAddress == null) {
		   userIpAddress = request.getRemoteAddr();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
        
%>
<br>
<br>
Your IP: <%= userIpAddress %>
<br>
<br>


<%
String recNo = null;
recNo = request.getParameter("recNo");
if(recNo == null){	
	System.out.println("rent, NULL ");	
	%>

	<jsp:forward page="/_errorPage.jsp">
		<jsp:param name="ERROR" value="release"/>
	</jsp:forward>

	<%
	
}
System.out.println("rent,recNo: " + recNo);
%>


You have canceled the following service:
<br>
<br>

<%
InterfaceClient_Buyer client = (InterfaceClient_Buyer)session.getAttribute("CON");
Record r = client.getRecord(Long.parseLong(recNo));
%>

Record Number: <%= r.getRecNo() %>
<br>
<br>
Name: <%= r.getName() %>
<br>
City: <%= r.getCity() %>
<br>
Types: <%= r.getTypesOfWork() %>
<br>
Staff: <%= r.getNumberOfStaff() %>
<br>
Rate: <%= r.getHourlyChargeRate() %>
<br>
Owner: <%= r.getOwner() %>
<br>
<br>

<%
	long cookieRelease = -1;
	boolean lockedRelease = false;
	try{
		cookieRelease = client.setRecordLocked(Long.parseLong(recNo));
		lockedRelease = client.releaseRecord(Long.parseLong(recNo), cookieRelease);
		client.setRecordUnlocked(Long.parseLong(recNo), cookieRelease);
	}catch(Exception e){String msg = e.getMessage();		
	%>
	<jsp:forward page="/_errorPage.jsp">
		<jsp:param name="ERROR" value="releaseREL"/>
		<jsp:param name="REL_MSG" value="<%= msg %>"/>
	</jsp:forward>
<%
}
%>

<form action="allRecordsBuyerRent.jsp" method="post">

	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	<input type="submit" value="Go back to Main page">
	
	
	
	
	
</form>
<br>
<br>
<br>
You can send you a receipt of the action.
<br>
<br>
<form action="SendMailServlet" method="post">
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	
	Your Emailaddress: <input type="text" name="EMAIL" size="30" value="peg-streifeneder@gmx.de">
	<br>
	<br>

	<input type="hidden" id="thisField" name="TEXT" value="<%= new String(
				"Thank you for using Bodgitt & Scarper introduced"
				+ "\nby Stefan Streifeneder"
				+ "\n\nYour ID: " + request.getParameter("ID")
 				+ "\n\nYour IP: " + request.getRemoteAddr()
				+ "\n\nDate: " + new Date()
 				+ "\n\n\nYou have released the following service: "
 				+ "\n\nRecord Number: " + r.getRecNo()
				+ "\nName: " + r.getName()
				+ "\nCity: " + r.getCity()
				+ "\nServices: " + r.getTypesOfWork()
				+ "\nStaff: " + r.getHourlyChargeRate()
				+ "\nOwner-ID: " + r.getOwner()
				+ "\n\nThank you very much."
				+ "\n\nBest regards"
				+ "\nStefan Streifeneder"
			
			) %>">
	<br>
	<br>
	<input type="submit" value="Send Email and go back to main Page">
	
	
	

	<input type="hidden" id="refer" name="REF" value="ID <%= request.getParameter("ID")%> 
															 - Auftragsbestätigung - release Service">
	
	
	
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