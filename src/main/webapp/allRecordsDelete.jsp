<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.seller.*" %>
<jsp:useBean id="connection" class="suncertify.sockets.seller.SocketClient_Seller" scope="session"/>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BS Table Delete</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<h2>Bodgitt and Scarper - SELLER</h2>
You have loaded the main page of the fictional Company Bodgitt and Scarper
introduced by Oracle.
<br>
<br>





Your ID : <%= request.getParameter("ID") %>	
	<br>
	<br>



<b>Delete</b>
If you want to remove your service you have to press the button 'Delete Record',
which loads the page to delete a service of reservation.
<br>
<br>
<b>Search</b>
You are enabled to investigate the database (please see below). Beware, if you are
connected to a MySQL database you have to enter the whole value of a field to get matches.
<br>
<br>







	<font size="+1" face="arial"><b>Services</b></font>
	<form action="delete.jsp" method="post">
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
List<Record> li = connection.getAllValidRecords();
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
		
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
		<input type="submit" value="Delete Record"> - Deletes the selected Record.
		If you have missed to select a Record an error page will be displayed.
	</form>
	
	
	<br>
	<form action="allRecordsFindByDelete.jsp" method="post">
	
		<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
		<input type="submit" name="search" value="SEARCH">
		<input type="text" name="searchText" size="64" value="devide fields by ';'">
	</form>

	<br>
	
	<form action="allRecordsVendor.jsp" method="post">
		<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
		<input type="submit" value="Go Back Seller Main Page">	
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