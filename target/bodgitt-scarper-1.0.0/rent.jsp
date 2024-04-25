<%@page import="java.net.InetAddress"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.buyer.*" %> 


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reservation Confirmation</title>
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

<%
String recNo = null;
recNo = request.getParameter("recNo");
if(recNo == null){
%>

	<jsp:forward page="/_errorPage.jsp">
		<jsp:param name="ERROR" value="rent"/>
	</jsp:forward>

<%	
}
InterfaceClient_Buyer client = (InterfaceClient_Buyer)session.getAttribute("CON");
Record r = client.getRecord(Long.parseLong(recNo));
%>
<br>
<br>
You have reserved the following service:
<br>
<br>	
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
Owner: <%= request.getParameter("ID") %>
<br>
<br>
<br>
<br>
<br>

<%
	boolean reserved = true;
	try{
		long cookie = client.setRecordLocked(Long.parseLong(recNo));	
		long rNo = Long.parseLong(recNo);
		int id = Integer.parseInt(request.getParameter("ID"));
		reserved = client.reserveRecord(rNo, id, cookie);	
		client.setRecordUnlocked(Long.parseLong(recNo), cookie);
	}catch(Exception e){
		String msg = e.getMessage();
		%>
		<jsp:forward page="/_errorPage.jsp">
			<jsp:param name="ERROR" value="rentRent"/>
			<jsp:param name="RENT_MSG" value="<%= msg %>"/>
		</jsp:forward>
	<%
		
	}
	
	if(!reserved){
		
		%>
		<jsp:forward page="/_errorPage.jsp">
			<jsp:param name="ERROR" value="rentRent"/>
			<jsp:param name="RENT_MSG" value="The ID Owner field was NOT blank!"/>
		</jsp:forward>
	<%
		
	}
	%>

<form action="allRecordsBuyerRent.jsp" method="post">

	<input type="hidden" id="thisAllRecordsBuyerRent" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	
	
	<input type="submit" value="Back to Main page">
</form>
<br>
<br>
<br>
You can send you a receipt of the action.
<br>
<br>


<form action="SendMailServlet" method="post">
	<input type="hidden" id="thisFieldSendMailServlet" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	Your Emailaddress: <input type="text" name="EMAIL" size="30" value="peg-streifeneder@gmx.de">
	<br>
	<br> 

	<input type="hidden" id="thisTextSendMailServlet" name="TEXT" value="<%= new String(
				"Thank you for using Bodgitt & Scarper introduced"
				+ "\nby Stefan Streifeneder"
				+ "\n\nYour ID: " + request.getParameter("ID")
 				+ "\n\nYour IP: " + request.getRemoteAddr()
				+ "\n\nDate: " + new Date()
 				+ "\n\n\nYou have rented the following service: "
 				+ "\n\nRecord Number: " + r.getRecNo()
				+ "\nName: " + r.getName()
				+ "\nCity: " + r.getCity()
				+ "\nServices: " + r.getTypesOfWork()
				+ "\nStaff: " + r.getHourlyChargeRate()
				+ "\nOwner-ID: " + request.getParameter("ID")
				+ "\n\nThank you very much."
				+ "\n\nBest regards"
				+ "\nStefan Streifeneder"
			
			) %>">

	<input type="hidden" id="refer" name="REF" value="ID <%= request.getParameter("ID")%> 
															 - Auftragsbestätigung - rent Service">
	<br>
	<br>
	<input type="submit" value="Send Email and go back to main Page">
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