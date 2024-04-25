<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.buyer.*" %>
 


<%@ page errorPage="_errorPage.jsp"%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BS Table Rent</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<h2>Bodgitt and Scarper - BUYER</h2>
You have loaded the main page of the fictional Company Bodgitt and Scarper
introduced by Oracle.
<br>
<br>
<%
String ipCon = (String)session.getAttribute("IP");
String portCon = (String)session.getAttribute("PORT");
InterfaceClient_Buyer client = (InterfaceClient_Buyer)session.getAttribute("CON");
%>


Your ID : <%= request.getParameter("ID") %>	
	<br>
	<br>
Server IP: <%= ipCon %>	
	<br>
	<br>
Server Port: <%= portCon %>


<br>
<br>
<b>Rent</b>
You are enabled to reserve an offered service by house improvement contractor.
After reserving a service your ID will be entered in the database automatically.
<br>
<br>
<b>Release</b>
If you want to remove your reservation you have to press the button 'RELEASE',
which loads the page to release a service of reservation.
<br>
<br>
<b>Search</b>
You are enabled to investigate the database (please see below). Beware, if you are
connected to a MySQL database you have to enter the value of a field to get matches.
<br>
<br>


	<font size="+1" face="arial"><b>Services</b></font>
	<form action="rent.jsp" method="post">
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	
		<table border=1 bgcolor="black" width="100%" align="center">
			<tr>
				<td>
					<table border="0" bgcolor="white" cellspacing=0 width="100%">
						<tr bgcolor="grey">
							<th>&nbsp;</th><th>Name</th><th>City</th><th>Types</th>
							<th>Staff</th><th>Rate</th><th>Owner</th>
						</tr>

<%
String showID = request.getParameter("ID");
List<Record> li = null;
try{
	li = client.getAllValidRecords();	
}catch(Exception e){
	e.printStackTrace();
}

Record rec;
for(Record r : li){
%>

						<tr>						
							<td>
							
								<input type="radio" name="recNo" 
									value="<%= r.getRecNo() %>">
							
								
							</td>
							<td>
								<%= r.getName() %>
							</td>
							<td>
								<%= r.getCity() %>
							</td>
							<td>
								<%= r.getTypesOfWork() %>
							</td>
							<td>
								<%= r.getNumberOfStaff() %>
							</td>
							<td>
								<%= r.getHourlyChargeRate() %>
							</td>
							<td>
								<%= r.getOwner() %>
							</td>
						</tr>
						
						
						
<% } %>
					</table>
				</td>
			</tr>
		</table>
		<p>	
		<input type="submit" value="RENT"> - Your ID will be entered
	</form>
	
	<form action="allRecordsRelease.jsp" method="post">
		<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
		<input type="submit" value="RELEASE"> - The page will be reloaded to support release actions
	</form>
	
	<br>	
	
  
	<form action="allRecordsFindByRelease.jsp" method="post">
		<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
		<input type="submit" name="search" value="SEARCH">
		<input type="text" name="searchText" size="64" value="devide fields by ';'">
	</form>

	<br>
	
	<form action="start.jsp" method="post">
		<input type="submit" value="Go Back to start">	
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