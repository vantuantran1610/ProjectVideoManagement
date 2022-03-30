package edu.vn.service;

import java.util.List;

import edu.vn.entity.History;
import edu.vn.entity.User;
import edu.vn.entity.Video;

public interface HistoryService {
	
	List<History> findByUser(String username);

	List<History> findByUserAndIsLiked(String username);

	History findByUserIdAndVideoId(Integer userId, Integer videoId);

	History create(User user, Video video);

	boolean updateLikeOrUnlike(User user, String videoHref);
}
