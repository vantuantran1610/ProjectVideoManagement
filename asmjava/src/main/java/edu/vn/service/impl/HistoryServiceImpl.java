package edu.vn.service.impl;

import java.sql.Timestamp;
import java.util.List;

import edu.vn.dao.HistoryDao;
import edu.vn.dao.impl.HistoryDaoImpl;
import edu.vn.entity.History;
import edu.vn.entity.User;
import edu.vn.entity.Video;
import edu.vn.service.HistoryService;
import edu.vn.service.VideoService;

public class HistoryServiceImpl implements HistoryService {

	private HistoryDao dao;

	private VideoService videoService = new VideoServiceImpl();

	public HistoryServiceImpl() {
		dao = new HistoryDaoImpl();
	}

	@Override
	public List<History> findByUser(String username) {
		return dao.findByUser(username);
	}

	@Override
	public List<History> findByUserAndIsLiked(String username) {
		return dao.findByUserAndIsLiked(username);
	}

	@Override
	public History findByUserIdAndVideoId(Integer userId, Integer videoId) {
		return dao.findByUserIdAndVideoId(userId, videoId);
	}

	@Override
	public History create(User user, Video video) {
		History exithistory = findByUserIdAndVideoId(user.getId(), video.getId());
		
		if(exithistory == null) {
			exithistory = new History();
			exithistory.setUser(user);
			exithistory.setVideo(video);
			exithistory.setViewedDate(new Timestamp(System.currentTimeMillis()));
			exithistory.setIsLiked(Boolean.FALSE);
			return dao.create(exithistory);
		}
		return exithistory;
	}

	@Override
	public boolean updateLikeOrUnlike(User user, String videoHref) {
		Video video = videoService.findByHref(videoHref);
		History exitHistory = findByUserIdAndVideoId(user.getId(), video.getId());

		if (exitHistory.getIsLiked() == Boolean.FALSE) {
			exitHistory.setIsLiked(Boolean.TRUE);
			exitHistory.setLikedDate(new Timestamp(System.currentTimeMillis()));
		} else {
			exitHistory.setIsLiked(Boolean.FALSE);
			exitHistory.setLikedDate(null);
		}

		History updatedHistory = dao.update(exitHistory);
		return updatedHistory != null ? true : false;
	}

}
