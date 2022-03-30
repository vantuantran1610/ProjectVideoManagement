package edu.vn.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import edu.vn.constant.VideoHref;
import edu.vn.dao.UserDao;
import edu.vn.dao.impl.UserDaoImpl;
import edu.vn.dto.UserDto;
import edu.vn.entity.User;
import edu.vn.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao dao;

	public UserServiceImpl() {
		dao = new UserDaoImpl();
	}

	@Override
	public User findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public User finByemail(String email) {
		return dao.finByemail(email);
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}

	@Override
	public User login(String username, String password) {
		return dao.findByUsernamePassword(username, password);
	}

	@Override
	public User resetPassword(String email) {
		User exitUser = finByemail(email);
		if (exitUser != null) {
			// (Math.random()) * ((max - min )) + min
			String newPass = String.valueOf((int) (Math.random() * ((9999 - 1000) + 1)) + 1000);
			exitUser.setPassword(newPass);
			return dao.update(exitUser);
		}
		return null;
	}

	@Override
	public List<User> findAll() {
		return dao.findAll();
	}

	@Override
	public List<User> findAll(int pageNumber, int pageSize) {
		return dao.findAll(pageNumber, pageSize);
	}

	@Override
	public User create(String username, String password, String email) {
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPassword(password);// ma hoa md5
		newUser.setEmail(email);
		newUser.setIsAdmin(Boolean.FALSE);
		newUser.setIsActive(Boolean.TRUE);
		return dao.create(newUser);
	}

	@Override
	public User update(User entity) {
		return dao.update(entity);
	}

	@Override
	public User delete(String username) {
		// khong xoa chi nap isActive de bao ton du lieu
		User user = dao.findByUsername(username);
		user.setIsActive(Boolean.FALSE);
		return dao.update(user);
	}

	@Override
	public List<UserDto> findUsersLikedVideoByVideoHref(String href) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(VideoHref.VIDEO_HREF, href);
		List<UserDto> result = new ArrayList<>();
		List<User> users = dao.findUsersLikedByVideoHref(params);
		users.forEach(user ->{
			UserDto dto = new UserDto();
			dto.setUsername(user.getUsername());
			dto.setEmail(user.getEmail());
			result.add(dto);
		});
		return result;
	}

}
