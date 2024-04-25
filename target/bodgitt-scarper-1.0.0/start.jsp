<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bodgitt and Scarper Start</title>
</head>
<body>
<h3>This application derives form Oracle Certified Java Developer (OCJD) application.</h3>
<h3>The OCJD app ist actually a stand alone client server application. </h3>
<h3>You can download the  OCJD app under the following link: 
    <a href="http://80.151.22.160:90/ss_hp/downloadStart.jsp" target="_blank">Bodgitt and Scarper download</a></h3>
<br/>__________________________________________________________________________________________________________
<br/>
<h1>Bodgitt and Scarper Web (BS-Web-App)</h1>
<h2>Your broker for home improving services</h2>
You have to register, please enter your ID:
<br>
<form action="chooseRole.jsp" method="post">
	<input type="text" name="ID" size="30" value="1">
	
	<input type="submit" value="Start">
</form>
<br>
<br>
<h3>Bodgitt and Scarper stand alone (BSapp)</h3>
Bodgitt and Scarper is a fictional company introduced by <b>O</b>racle <b>C</b>ertified 
<b>J</b>ava <b>D</b>eveloper (<b>OCJD</b>/OCJDapp) for the Java 2 Platform: Application Submission 
(Version 2.2.2).
<br>
The software(BSapp) required by the submission is first of all a so called stand alone application.
A stand alone application has its own frame/window and it is started via command line or the directory
with a double click on the file. It does not use nor a browser or a http server.
BSapp provides beside other functionalities a full-fletched
server, which can be run over the internet so far a static ip is adjusted.
<br>
<br>
<br>
<h3>Bodgitt and Scarper Web (BS-Web-App)</h3>

BS-Web-App refers to BSapp and represents the web interface to connect
with BSapp. BSapp has to be started in advance to get BS-Web-App contected.

<br>
<br>
<br>

This application (BSapp) includes the entire application (OCJDapp) required by Oracle. 
BSapp is a Java Dynamic Web Application packed as a .war file, which has to be located in the 
directory webapps of the Apache Tomcat web server eventually. All classes of OCJDapp
are stored in the directory 'Java Resources'. Usually OCJDapp will be provided as a JAR.
<br>
BSapp provides the .jsp files and OCJDapp provides the .java files. OCJDapp handles the 
communication between the Database Server and the client.
<br>
<br>
<h3>Database Server</h3>
The application provides access to a database via an interface by using the classes of
OCJDapp and does not access the database directly by SQL commands. The implementer class of the 
interface provides a method, which reads and sends byte code to an database server
by ip address and port number. This design is based on the certificate OCJD. 
<br>
Afterwards the communication between the interface of this application and the server
consists of byte code, it is up to the server how to access the database. I provide
a MySQL database server and a no-relational database server, which can be accessed by 
this application. The server applications are Java Standalone Applications and they are
provided by executable JARs, which can be downloaded at my homepage 
http://217.7.195.18/ss_hp/downloads.
<br>
<br>
<h3>Connection to the Database server</h3>
The application does not use a Java Bean to connect to the server. It creates objects to connect
to the server, which will be set as session attributes. A new session can be created
manually.
<br>
<br>
<h3>Bodgitt an Scarper - The Background Story</h3>
Bodgitt and Scarper, LLC. is a broker of home improvement contractors.
They take requests from home owners for a type of service, and offer
the homeowner one or more contractors that can provide the required services.
Curently, Bodgitt and Scarper provides this service over the phone using a team of
customer service representatives (CSRs). The CSRs interact with an
ageing custom-written application that has been drawing increasing criticism
from the CSRs. In the future, Bodgitt and Scarper wants to move into
Internet-based marketing, and hopes to be able to provide their services
directly to customers over the web.
<P>The company's IT director has decided to migrate the existing
application to a Java technology based system. Initially, the system
will support only the CSRs, although the hope is that this interim
step will give them a starting point for migrating the system to the
web. The IT director does not anticipate much reuse of the first Java
technology system, but intends to use that system as a learning
exercise before going on to a web based system.
<P>The company's IT department has a data file that contains the
essential information for the company, but because the data must
continue to be manipulated for reports using another custom-written
application, the new system must reimplement the database code from
scratch without altering the data file format.
<P>The new application, using the existing data file format, must
allow the CSRs to generate a list of constractors that match a
customer's criteria. This is the project that you have been
commissioned to implement.
<br>
<br>
<br>
<br>
Author: stefan.streifeneder@gmx.de
<br>
<br>
</body>
</html>