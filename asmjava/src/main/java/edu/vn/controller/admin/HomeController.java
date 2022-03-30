package edu.vn.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.vn.constant.SessionAttr;
import edu.vn.dto.UserDto;
import edu.vn.dto.VideoLikedInfo;
import edu.vn.entity.User;
import edu.vn.service.StatsService;
import edu.vn.service.UserService;
import edu.vn.service.impl.StatsServiceImpl;
import edu.vn.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = { "/admin", "/admin/favorites" }, name = "HomeControllerOfAdmin")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 581765977725452134L;
	private StatsService statsService = new StatsServiceImpl();
	private UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);

		if (currentUser != null && currentUser.getIsAdmin() == Boolean.TRUE) {
			String path = req.getServletPath();

			switch (path) {
			case "/admin":
				doGetHome(req, resp);
				break;
			case "/admin/favorites":
				doGetFavorites(req, resp);
				break;
			}
		} else {
			resp.sendRedirect("index");
		}
	}

	private void doGetHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<VideoLikedInfo> videos = statsService.findVideoLikedInfo();
		req.setAttribute("videos", videos);
		RequestDispatcher rd = req.getRequestDispatcher("views/admin/home.jsp");
		rd.forward(req, resp);
	}

	private void doGetFavorites(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		String videoHref = req.getParameter("href");
		List<UserDto> users = userService.findUsersLikedVideoByVideoHref(videoHref);

		if (users.isEmpty()) {
			resp.setStatus(400);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			String dataResponse = mapper.writeValueAsString(users);
			resp.setStatus(200);
			out.print(dataResponse);
			out.flush();
		}
	}

}
