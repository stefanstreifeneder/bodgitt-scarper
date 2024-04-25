<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.seller.*" %>
<%@ page errorPage="_errorPage.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BS Update Record</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>
New Record will be displayed
<br>
<br>


<%
String recNo = request.getParameter("recNo");
String name = request.getParameter("name");
String city = request.getParameter("city");
String types = request.getParameter("types");
String staff = request.getParameter("staff");
String rate = request.getParameter("rate");
String owner = request.getParameter("owner");
InterfaceClient_Seller client = (InterfaceClient_Seller)session.getAttribute("CON");
Record r = client.getRecord(Long.parseLong(recNo));
%>

Record Number: <%= r.getRecNo() %>
<br>
<br>
Name: <%= name %>
<br>
City: <%= city %>
<br>
Types: <%= types %>
<br>
Staff: <%= staff %>
<br>
Rate: <%= rate %>
<br>
Owner: <%= owner %>
<br>

<br>
<br>
<br>
<br>

<form action="allRecordsVendor.jsp" method="post">
		<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
		<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
		<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	<%
	long cookie = -1;
	try{
		cookie = client.setRecordLocked(Long.parseLong(recNo));
		boolean locked = client.modifyRecord(new Record(Long.parseLong(recNo),
				name,
				city, types, Integer.parseInt(staff), rate, owner), 
				Long.parseLong(recNo), cookie);
		client.setRecordUnlocked(Long.parseLong(recNo), cookie);
	}catch(Exception e){
		if(!(e instanceof RecordNotFoundException)){
			client.setRecordUnlocked(Long.parseLong(recNo), cookie);
		}		
		String msg = e.getMessage();
		
		%>
		<jsp:forward page="/_errorPage.jsp">
			<jsp:param name="ERROR" value="updateNewRec"/>
			<jsp:param name="UP_MSG" value="<%= msg %>"/>
		</jsp:forward>
	<%
		
	}
	%>
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
															 - Auftragsbestätigung - update a Service">
	
	Your Emailaddress: <input type="text" name="EMAIL" size="30" value="peg-streifeneder@gmx.de">
	<br>
	<br>
	<input type="hidden" id="thisField" name="TEXT" value="<%= new String(
				"Thank you for using Bodgitt & Scarper introduced"
				+ "\nby Stefan Streifeneder"
				+ "\n\nYour ID: " + request.getParameter("ID")
 				+ "\n\nYour IP: " + request.getRemoteAddr()
				+ "\n\nDate: " + new Date()
 				+ "\n\n\nYou have updated the following service: "
 				+ "\n\nRecord Number: " + r.getRecNo()
				+ "\nName: " + name
				+ "\nCity: " + city
				+ "\nServices: " + types
				+ "\nStaff: " + rate
				+ "\nOwner-ID: " + owner
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