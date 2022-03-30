package edu.vn.service;

import javax.servlet.ServletContext;

import edu.vn.entity.User;

public interface EmailService {
	void sendEMail(ServletContext context, User recipient, String type);
}
