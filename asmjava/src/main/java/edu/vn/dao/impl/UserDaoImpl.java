package edu.vn.dao.impl;

import java.util.List;
import java.util.Map;

import edu.vn.constant.NamdStored;
import edu.vn.dao.AbstractDao;
import edu.vn.dao.UserDao;
import edu.vn.entity.User;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {

	@Override
	public User findById(Integer id) {
		return super.findById(User.class, id);
	}

	@Override
	public User finByemail(String email) {
		String sql = "SELECT o FROM User o WHERE o.email = ?0";
		return super.findOne(User.class, sql, email);
	}

	@Override
	public User findByUsername(String username) {
		String sql = "SELECT o FROM User o WHERE o.username = ?0";
		return super.findOne(User.class, sql, username);
	}

	@Override
	public User findByUsernamePassword(String username, String password) {
		String sql = "SELECT o FROM User o WHERE o.username = ?0 AND o.password = ?1";
		return super.findOne(User.class, sql, username, password);
	}

	@Override
	public List<User> findAll() {
		return super.findAll(User.class, true);

	}

	@Override
	public List<User> findAll(int pageNumber, int pageSize) {
		return super.findAll(User.class, true, pageNumber, pageSize);
	}

	@Override
	public List<User> findUsersLikedByVideoHref(Map<String, Object> params) {
		
		return super.callStored(NamdStored.FIND_USERS_LIKED_VIDEO_BY_VIDEO_HREF, params);
	}

}
