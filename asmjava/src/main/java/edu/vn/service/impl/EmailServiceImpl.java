package edu.vn.service.impl;

import javax.servlet.ServletContext;

import edu.vn.entity.User;
import edu.vn.service.EmailService;
import edu.vn.util.SendEmailUtil;

public class EmailServiceImpl implements EmailService {
	private static final String EMAIL_WELLCOME_SUBJECT = "Wellcome to Online Entertainment";
	private static final String EMAIL_FORGOT_PASSWORD = "Online Entertainment - New password";

	@Override
	public void sendEMail(ServletContext context, User recipient, String type) {
		String host = context.getInitParameter("host");
		String port = context.getInitParameter("port");
		String user = context.getInitParameter("user");
		String pass = context.getInitParameter("pass");

		try {
			String content = null;
			String subject = null;
			switch (type) {
			case "wellcome": 
				subject = EMAIL_WELLCOME_SUBJECT;
				content = "Dear " + recipient.getUsername() + ", hope you have a good time!";
				break;
			case "forgot": 
				subject = EMAIL_FORGOT_PASSWORD;
				content = "Dear " + recipient.getUsername() + ", your new password here: " + recipient.getPassword();
				break;
			default:
				subject = "Online Entertainment";
				content = "Maybe this email is wrong, dont't care about it";
			}
			SendEmailUtil.sendEmail(host, port, user, pass, recipient.getEmail(), subject, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
