package com.cronoteSys.util;

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

	/**
	 * 
	 * Try to send a email message according to the parameters
	 * 
	 * @param sEmailTo the recipient of the email message
	 * @return true if email was sent and false if not
	 * @author Fernando
	 */
	public boolean sendEmail(String sEmailTo) throws EmailException {
		try {
			Email email = new SimpleEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("testesmpt@gmail.com", "Omega1390-"));
			email.setSSLOnConnect(true);
			email.setFrom("testesmpt@gmail.com");
			email.setSubject("Testando");
			email.setMsg("Isso aqui é um testão");
			email.addTo(sEmailTo);
			email.send();
			return true;
		} catch (EmailException e) {
			e.getMessage();
		}
		return false;
	}
	
	public boolean sendEmail(String sEmailTo,String sMessage, String sSubject) throws EmailException {
		try {
			Email email = new SimpleEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("cronotesys@gmail.com", "Cro12345"));
			email.setSSLOnConnect(true);
			email.setFrom("cronotesys@gmail.com","Cronote");
			email.setSubject(sSubject);
			email.setMsg(sMessage);
			email.addTo(sEmailTo);
			email.send();
			return true;
		} catch (EmailException e) {
			e.getMessage();
		}
		return false;
	}

	public boolean validateEmail(String sEmail) {
		Pattern p = Pattern.compile("^([A-z]+)([A-z0-9-_.]*)@([A-z.]+)\\.[A-z]{2,}$");
		Matcher m = p.matcher(sEmail);
		return m.matches();
	}
}
