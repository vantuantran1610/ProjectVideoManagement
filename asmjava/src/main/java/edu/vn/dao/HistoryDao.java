package edu.vn.dao;

import java.util.List;

import edu.vn.entity.History;

public interface HistoryDao {

	List<History> findByUser(String username);

	List<History> findByUserAndIsLiked(String username);

	History findByUserIdAndVideoId(Integer userId, Integer videoId);

	History create(History entity);

	History update(History entity);

}
