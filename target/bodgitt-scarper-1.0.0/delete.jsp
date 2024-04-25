<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.seller.*" %>
<%@ page errorPage="_errorPage.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BS Seller Delete</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>
Thank for using Bodgitt and Scarper introduced by Oracle.

<%   

System.out.println("delete, START");
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

You have deleted the following Record:

<br>
<br>
<br>

<%
System.out.println("rent, before get recNo");
String recNo = null;
recNo = request.getParameter("recNo");
if(recNo == null){	
	System.out.println("rent, NULL ");	
	%>

	<jsp:forward page="/_errorPage.jsp">
		<jsp:param name="ERROR" value="delete"/>
	</jsp:forward>

	<%
	
}
System.out.println("rent,recNo: " + recNo);
InterfaceClient_Seller client = (InterfaceClient_Seller)session.getAttribute("CON");
Record r = client.getRecord(Long.parseLong(recNo));
%>






<form action="allRecordsVendor.jsp" method="post">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
		<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	

Record Number: <%= r.getRecNo() %>
<br>
<br>
Record Number: <%= r.getRecNo() %> 
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
<br>
<br>
<br>
<%
	long cookie = -1;
	try{
		cookie = client.setRecordLocked(Long.parseLong(recNo));
		client.removeRecord(Long.parseLong(recNo), cookie);
		client.setRecordUnlocked(Long.parseLong(recNo), cookie);
	}catch(Exception e){			
		String msg = e.getMessage();		
		%>
		<jsp:forward page="/_errorPage.jsp">
			<jsp:param name="ERROR" value="deleteErr"/>
			<jsp:param name="DEL_MSG" value="<%= msg %>"/>
		</jsp:forward>
	<%
	}
%>


	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="submit" value="Show all Records">
</form>


<br>
<br>
<br>
You can send you a receipt of the action.
<br>
<br>
<form action="SendMailServlet" method="post">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
		<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="isSeller" name="ROLE" value="seller">
	<input type="hidden" id="refer" name="REF" value="ID <%= request.getParameter("ID")%> 
															 - Auftragsbestätigung - delete Service">
	
	Your Emailaddress: <input type="text" name="EMAIL" size="30" value="peg-streifeneder@gmx.de">
	<br>
	<br>
	<input type="hidden" id="thisField" name="TEXT" value="<%= new String(
				"Thank you for using Bodgitt & Scarper introduced"
				+ "\nby Stefan Streifeneder"
				+ "\n\nYour ID: " + request.getParameter("ID")
 				+ "\n\nYour IP: " + request.getRemoteAddr()
				+ "\n\nDate: " + new Date()
 				+ "\n\n\nYou have deleted the following service: "
 				+ "\n\nRecord Number: " + r.getRecNo()
				+ "\n\nName: " + r.getName()
				+ "\nCity: " + r.getTypesOfWork()
				+ "\nServices: " + r.getNumberOfStaff()
				+ "\nStaff: " + r.getHourlyChargeRate()
				+ "\nOwner-ID: " + r.getOwner()
				+ "\n\nThank you very much."
				+ "\n\nBest regards"
				+ "\nStefan Streifeneder"
			
			) %>">
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