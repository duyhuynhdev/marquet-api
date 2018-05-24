package com.marqet.WebServer.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class SendMail {

	private final String msgFrom = "noreply.marqet@gmail.com";
	private final String username = "noreply.marqet";
	private final String password = "marqetdeveloper";
	private String msgTo;
	private String msgSubject;
	private String msgBody;
	
	public SendMail(String msgTo, String msgSubject, String msgBody) throws UnsupportedEncodingException{
		this.msgTo = msgTo;
		this.msgSubject = msgSubject;
		this.msgBody = msgBody;
		this.send();
	}
	
	public void send(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Properties props = System.getProperties();
				props.put("mail.smtp.host", "smtp.googlemail.com");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.fallback", "false");
				
				Session mailSession = Session.getDefaultInstance(props, null);
				//mailSession.setDebug(true);
				
		        try {
		        	Message mailMessage = new MimeMessage(mailSession);
					mailMessage.setFrom(new InternetAddress(msgFrom, username));
					mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(msgTo));
					mailMessage.setSubject(msgSubject);
					mailMessage.setContent(msgBody, "text/html; charset=utf-8");
		            Transport transport = mailSession.getTransport("smtp");
		            transport.connect("smtp.googlemail.com", username, password);
		            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		            transport.close();

		        } catch (MessagingException | UnsupportedEncodingException e) {
					e.printStackTrace();
		        }
			}
		}).start();
	}
}
