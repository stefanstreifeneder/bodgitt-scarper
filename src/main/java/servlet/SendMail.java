package servlet;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	
	public static void sendGMX() throws MessagingException
	{
	    String sender = "stefan.streifeneder@gmx.de";
	    String password = "089fritz{}AN";
	    String receiver = "peg-streifeneder@gmx.de";

	    final Properties properties = new Properties();

	    properties.put("mail.transport.protocol", "smtp");
	    properties.put("mail.smtp.host", "mail.gmx.net");
	    properties.put("mail.smtp.port", "587");
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.user", sender);
	    properties.put("mail.smtp.password", password);
	    properties.put("mail.smtp.starttls.enable", "true");

	    Session mailSession = Session.getInstance(properties, new Authenticator()
	    {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication()
	        {
	            return new PasswordAuthentication(properties.getProperty("mail.smtp.user"),
	                    properties.getProperty("mail.smtp.password"));
	        }
	    });

	    Message message = new MimeMessage(mailSession);
	    InternetAddress addressTo = new InternetAddress(receiver);
	    message.setRecipient(Message.RecipientType.TO, addressTo);
	    message.setFrom(new InternetAddress(sender));
	    message.setSubject("The subject");
	    message.setContent("This is the message content", "text/plain");
	    Transport.send(message);
	}
	
	public static void sendGMX(String receiver, String refer, String msg) throws MessagingException
	{
	    String sender = "stefan.streifeneder@gmx.de";
	    String password = "089fritz{}AN";
	    //String receiver = "peg-streifeneder@gmx.de";

	    final Properties properties = new Properties();

	    properties.put("mail.transport.protocol", "smtp");
	    properties.put("mail.smtp.host", "mail.gmx.net");
	    properties.put("mail.smtp.port", "587");
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.user", sender);
	    properties.put("mail.smtp.password", password);
	    properties.put("mail.smtp.starttls.enable", "true");

	    Session mailSession = Session.getInstance(properties, new Authenticator()
	    {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication()
	        {
	            return new PasswordAuthentication(properties.getProperty("mail.smtp.user"),
	                    properties.getProperty("mail.smtp.password"));
	        }
	    });

	    Message message = new MimeMessage(mailSession);
	    InternetAddress addressTo = new InternetAddress(receiver);
	    message.setRecipient(Message.RecipientType.TO, addressTo);
	    message.setFrom(new InternetAddress(sender));
	    message.setSubject(refer);
	    message.setContent(msg, "text/plain");
	    Transport.send(message);
	}
	
	
//    public static void main(String[] args) {
//    	System.out.println("SendMail START");
////      SendMail send_mail    =   new SendMail();
//      
//      try {
//    	  SendMail.sendGMX();
//	} catch (MessagingException e) {
//		e.printStackTrace();
//	}
////      String empName = "xxxxx";
////      String title ="<b>Hi !"+empName+"</b>";
////      send_mail.sendMail("stefan.streifeneder@gmx.de", 
////    		  "peg-streifeneder@gmx.de", "Please apply for leave for the following dates", 
////    		  title+"<br>by<br><b>HR<b>");
//      
//      
//      System.out.println("SendMail END");
//    }

}
