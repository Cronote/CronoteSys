package com.cronoteSys.util;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Class that cares about email's functions like to send email messages and to
 * validate email's format
 * 
 * @author Bruno, Fernando
 * 
 */
public class EmailUtil {
	private final String sUser = "cronotesys@gmail.com";
	private final String sPass = "qfkmpdycmduuzfmr";
	private final int iSmtpPort = 465;
	private final String sHostName = "smtp.googlemail.com";

	/**
	 * 
	 * Try to send a email message according to the parameters
	 * 
	 * @param sEmailTo the recipient of the email message
	 * @return true if email was sent and false if not
	 * @author Fernando
	 */
	
	public boolean sendEmail(String sEmailTo,String sMessage, String sSubject) throws EmailException {
		try {
			Email email = new SimpleEmail();
			email.setHostName(sHostName);
			email.setSmtpPort(iSmtpPort);
			email.setAuthenticator(new DefaultAuthenticator(sUser, sPass));
			email.setSSLOnConnect(true);
			email.setFrom(sUser,"Cronote");
			email.setSubject(sSubject);
			email.setMsg(sMessage);
			email.addTo(sEmailTo);
			sendEmail(email);
			return true;
		} catch (EmailException e) {
			e.getMessage();
		}
		return false;
	}
	
	public void sendEmail(Email email) {
		CompletableFuture.runAsync(() ->{
			try {
				email.send();
			} catch (EmailException e) {
				e.printStackTrace();
			}
		});
	}
	
	

	public static boolean validateEmail(String sEmail) {
		Pattern p = Pattern.compile("^([A-z]+)([A-z0-9-_.]*)@([A-z.]+)\\.[A-z]{2,}$");
		Matcher m = p.matcher(sEmail);
		return m.matches();
	}
}
