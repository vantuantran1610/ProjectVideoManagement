package edu.vn.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vn.constant.SessionAttr;
import edu.vn.entity.User;
import edu.vn.entity.Video;
import edu.vn.service.VideoService;
import edu.vn.service.impl.VideoServiceImpl;

@WebServlet(urlPatterns = { "/admin/video" }, name = "VideoControllerOfAdmin")
public class VideoController extends HttpServlet {

	private static final long serialVersionUID = 1261804179059768237L;
	private VideoService videoService = new VideoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);

		if (currentUser != null && currentUser.getIsAdmin() == Boolean.TRUE) {
			String action = req.getParameter("action");
			switch (action) {
			case "view":
				doGetOverView(req, resp);
				break;

			case "delete":
				doGetDelete(req, resp);
				break;

			case "add":
				req.setAttribute("isEdit", false);
				doGetAdd(req, resp);
				break;
				
			case "edit":
				req.setAttribute("isEdit", true);
				doGetEdit(req, resp);
				break;
			}
		} else {
			resp.sendRedirect("index");
		}

		// asmjava.admin.video?action=view
		// asmjava.admin.video?action=edit&href={href}
	}

	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);

		if (currentUser != null && currentUser.getIsAdmin() == Boolean.TRUE) {
			String action = req.getParameter("action");
			switch (action) {
			case "add":
				doPostAdd(req, resp);
				break;
			case "edit":
				doPostEdit(req, resp);
				break;
			}
		} else {
			resp.sendRedirect("index");
		}
	}

	private void doGetOverView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Video> videos = videoService.findAll();
		req.setAttribute("videos", videos);
		RequestDispatcher rd = req.getRequestDispatcher("/views/admin/video-overview.jsp");
		rd.forward(req, resp);
	}

	private void doGetDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String href = req.getParameter("href");
		Video videoDeleted = videoService.delete(href);

		if (videoDeleted != null) {
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}

	private void doGetAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/views/admin/video-edit.jsp");
		rd.forward(req, resp);
	}
	
	private void doGetEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String href = req.getParameter("href");
		Video video = videoService.findByHref(href); 
		req.setAttribute("video", video);
		RequestDispatcher rd = req.getRequestDispatcher("/views/admin/video-edit.jsp");
		rd.forward(req, resp);
	}
	private void doPostAdd(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");
		String title = req.getParameter("title");
		String href = req.getParameter("href");
		String description = req.getParameter("description");
		String poster = req.getParameter("poster");
		Video video = new Video();
		video.setTitle(title);
		video.setHref(href);
		video.setDescription(description);
		video.setPoster(poster);
		
		Video videoReturn = videoService.create(video);

		if (videoReturn != null) {
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}
	
	private void doPostEdit(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");
		String title = req.getParameter("title");
		String href = req.getParameter("newhref");
		String description = req.getParameter("description");
		String poster = req.getParameter("poster");
		String hrefOrigin = req.getParameter("hrefOrigin");
		
		
		Video video = videoService.findByHref(hrefOrigin);
		video.setTitle(title);
		video.setHref(href);
		video.setDescription(description);
		video.setPoster(poster);
		
		Video videoReturn = videoService.update(video);

		if (videoReturn != null) {
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}
}
