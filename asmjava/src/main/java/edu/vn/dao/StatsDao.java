package edu.vn.dao;

import java.util.List;

import edu.vn.dto.VideoLikedInfo;

public interface StatsDao {

	List<VideoLikedInfo> findVideoLikedInfo();

}
