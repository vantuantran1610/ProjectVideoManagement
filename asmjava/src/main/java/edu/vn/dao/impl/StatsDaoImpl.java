package edu.vn.dao.impl;

import java.util.ArrayList;
import java.util.List;

import edu.vn.dao.AbstractDao;
import edu.vn.dao.StatsDao;
import edu.vn.dto.VideoLikedInfo;

public class StatsDaoImpl extends AbstractDao<Object[]> implements StatsDao {

	@Override
	public List<VideoLikedInfo> findVideoLikedInfo() {
		String sql = "select v.id, v.title, v.href, sum(cast(h.isLiked as int)) as totalLike"
				+ " from video v left join history h on v.id = h.videoId" + " where v.isActive = 1"
				+ " group by v.id, v.title, v.href" + " order by sum(cast(h.isLiked as int)) desc";
		List<Object[]> objects = super.findManyByNativeQuery(sql);
		List<VideoLikedInfo> result = new ArrayList<>();
		objects.forEach(object -> {
			VideoLikedInfo VideoLikedInfo = setDataVideoLikedInfo(object);
			result.add(VideoLikedInfo);
		});
		return result;
	}

	private VideoLikedInfo setDataVideoLikedInfo(Object[] object) {
		VideoLikedInfo VideoLikedInfo = new VideoLikedInfo();
		VideoLikedInfo.setVideoId((Integer) object[0]);
		VideoLikedInfo.setTitle((String) object[1]);
		VideoLikedInfo.setHref((String) object[2]);
		VideoLikedInfo.setTotalLike(object[3] == null ? 0 : (Integer) object[3]);
		return VideoLikedInfo;
	}

}
