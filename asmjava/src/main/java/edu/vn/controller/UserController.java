package edu.vn.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vn.constant.SessionAttr;
import edu.vn.entity.User;
import edu.vn.service.EmailService;
import edu.vn.service.UserService;
import edu.vn.service.impl.EmailServiceImpl;
import edu.vn.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = { "/login", "/logout", "/register", "/forgotPass", "/changePass" })
public class UserController extends HttpServlet {

	private static final long serialVersionUID = -7071232318994824710L;

	private UserService userService = new UserServiceImpl();

	private EmailService emailService = new EmailServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String path = req.getServletPath(); // /login, /register
		switch (path) {
		case "/login":
			doGetLogin(req, resp);
			break;
		case "/register":
			doGetRegister(req, resp);
			break;
		case "/logout":
			doGetLogout(session, req, resp);
			break;
		case "/forgotPass":
			doGetForgotPass(req, resp);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String path = req.getServletPath();
		switch (path) {
		case "/login":
			doPostLogin(session, req, resp);
			break;
		case "/register":
			doPostRegister(session, req, resp);
			break;
		case "/forgotPass":
			doPostForgotPass(req, resp);
			break;
		case "/changePass":
			doPostChangePass(session, req, resp);
			break;
		}
	}

	private void doGetLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/views/user/login.jsp");
		rd.forward(req, resp);
	}

	private void doGetRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/views/user/register.jsp");
		rd.forward(req, resp);
	}

	private void doGetLogout(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		session.removeAttribute(SessionAttr.CURRENT_USER);
		resp.sendRedirect("index");
	}

	private void doGetForgotPass(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/views/user/forgotpass.jsp");
		rd.forward(req, resp);

	}

	private void doPostLogin(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		User user = userService.login(username, password);

		if (user != null) {
			session.setAttribute(SessionAttr.CURRENT_USER, user);
			resp.sendRedirect("index");
		} else {
			resp.sendRedirect("login");
		}
	}

	private void doPostRegister(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");

		User user = userService.create(username, password, email);

		if (user != null) {
			emailService.sendEMail(getServletContext(), user, "wellcome");
			session.setAttribute(SessionAttr.CURRENT_USER, user);
			resp.sendRedirect("index");
		} else {
			resp.sendRedirect("register");
		}
	}

	private void doPostForgotPass(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");
		String email = req.getParameter("email");
		User userWithNewPass = userService.resetPassword(email);

		if (userWithNewPass != null) {
			emailService.sendEMail(getServletContext(), userWithNewPass, "forgot");
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}

	private void doPostChangePass(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");
		String currentPass = req.getParameter("currentPass");
		String newPass = req.getParameter("newPass");

		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);

		if (currentUser.getPassword().equals(currentPass)) {
			currentUser.setPassword(newPass);
			User updatedUser = userService.update(currentUser);
			if (updatedUser != null) {
				session.setAttribute(SessionAttr.CURRENT_USER, updatedUser);
				resp.setStatus(204);
			} else {
				resp.setStatus(400);
			}
		} else {
			resp.setStatus(400);
		}

	}

}
