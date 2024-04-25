<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, suncertify.db.*, suncertify.sockets.buyer.*" %>
<%@ page errorPage="_errorPage.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BS Find By Release</title>
</head>
<body>
<h3>---- Connection by Object ----</h3>
<br>





This page shows all Records found by filter.


<%
String input = request.getParameter("searchText");
String[] tokens = input.split(";");

InterfaceClient_Buyer client = (InterfaceClient_Buyer)session.getAttribute("CON");
long[] recNos = client.findByFilter(tokens);
List<Record> li = new ArrayList<Record>();
for(int i = 0; i < recNos.length; i++){
	try {
		li.add(client.getRecord(recNos[i]));
	} catch (final RecordNotFoundException e) {
	}
}

%>





	<font size="+2" face="arial"><b>Conference Registration</b></font>
	<form action="allRecordsRelease.jsp" method="post">
	
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
Record rec;
for(Record r : li){
%>




						<tr>						
							<td>
							
								
							
								
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
		<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
		<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
		<input type="submit" value="back to Release page">
	</form>
<form action="allRecordsBuyerRent.jsp" method="post">
	<input type="hidden" id="thisField" name="ID" value="<%= request.getParameter("ID") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="IP" value="<%= session.getAttribute("IP") %>">
	<input type="hidden" id="thisAllRecordsBuyerRent" name="PORT" value="<%= session.getAttribute("PORT") %>">
	
	<input type="submit" value="back to Rent page"> 
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