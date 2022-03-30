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
import edu.vn.entity.History;
import edu.vn.entity.User;
import edu.vn.entity.Video;
import edu.vn.service.HistoryService;
import edu.vn.service.VideoService;
import edu.vn.service.impl.HistoryServiceImpl;
import edu.vn.service.impl.VideoServiceImpl;

@WebServlet(urlPatterns = "/video")
public class VideoController extends HttpServlet {

	private static final long serialVersionUID = 3650738256040345573L;

	private VideoService videoService = new VideoServiceImpl();

	private HistoryService historyService = new HistoryServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionParam = req.getParameter("action");
		String href = req.getParameter("id");

		HttpSession session = req.getSession();

		switch (actionParam) {
		case "watch":
			doGetWatch(session, href, req, resp);
			break;
		case "like":
			doGetLike(session, href, req, resp);
			break;
		}
	}

	private void doGetWatch(HttpSession session, String href, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Video video = videoService.findByHref(href);
		req.setAttribute("video", video);
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);

		if (currentUser != null) {
			History history = historyService.create(currentUser, video);
			req.setAttribute("flagLikedBtn", history.getIsLiked());
		}

		RequestDispatcher rd = req.getRequestDispatcher("/views/user/video-detail.jsp");
		rd.forward(req, resp);

	}

	private void doGetLike(HttpSession session, String href, HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		boolean result = historyService.updateLikeOrUnlike(currentUser, href);

		if (result == true) {
			resp.setStatus(204); // succeed but response data
		} else {
			resp.setStatus(400); // loi
		}
	}

	// localhost://8080/asmjava/video?action=watch&id={href}
}
