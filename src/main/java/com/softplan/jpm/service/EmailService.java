package com.softplan.jpm.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softplan.jpm.controller.JudicialProcessController;
import com.softplan.jpm.entities.Person;

public class EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	private static String from = "";
	private static String host = "";
	private static int port = 0;
	private static String username = "";
	private static String password = "";
	private static Properties smtpProp;

	static {

		try {
			
			LOGGER.info("Creating a new instance of email service");
			
			LOGGER.info("Reading smtp.properties file");    	

			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			System.out.println("rootPath = " + rootPath);
			String smtpConfigPath = rootPath + "smtp.properties";

			smtpProp = new Properties(); 	
			smtpProp.load(new FileInputStream(smtpConfigPath));
			
			from = smtpProp.getProperty("mail.smtp.from");
			host = smtpProp.getProperty("mail.smtp.host");
			port = Integer.parseInt(smtpProp.getProperty("mail.smtp.port"));
			username = smtpProp.getProperty("mail.smtp.username");
			password = smtpProp.getProperty("mail.smtp.password");
			
			LOGGER.debug("E-mail Properties:");			
			smtpProp.forEach((k, v) -> LOGGER.debug(k + "=" + v));
			
		}catch (Exception e) {
			LOGGER.error("Error reading smtp.properties");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void sendMail(Person person, String requestMessage) {

		Session session = Session.getInstance(smtpProp, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message emailsMessage = new MimeMessage(session);
			emailsMessage.setFrom(new InternetAddress(from));
			emailsMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(person.getEmail()));
			emailsMessage.setSubject("Judicial Process Email");			

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(requestMessage, "text/html");

			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			attachmentBodyPart.attachFile(new File("pom.xml"));

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			multipart.addBodyPart(attachmentBodyPart);

			emailsMessage.setContent(multipart);
			System.out.println("");
			//Transport.send(emailsMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

