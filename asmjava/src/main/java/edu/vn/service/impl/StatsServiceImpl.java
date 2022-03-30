package edu.vn.service.impl;

import java.util.List;

import edu.vn.dao.StatsDao;
import edu.vn.dao.impl.StatsDaoImpl;
import edu.vn.dto.VideoLikedInfo;
import edu.vn.service.StatsService;

public class StatsServiceImpl implements StatsService {

	private StatsDao statsDao;

	public StatsServiceImpl() {
		statsDao = new StatsDaoImpl();
	}

	@Override
	public List<VideoLikedInfo> findVideoLikedInfo() {

		return statsDao.findVideoLikedInfo();
	}

}
