package edu.vn.service;

import java.util.List;

import edu.vn.dto.VideoLikedInfo;

public interface StatsService {
	List<VideoLikedInfo> findVideoLikedInfo();
	
}
