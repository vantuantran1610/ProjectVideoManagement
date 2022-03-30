package edu.vn.dao;

import java.util.List;
import java.util.Map;

import edu.vn.entity.User;

public interface UserDao {

	User findById(Integer id);

	User finByemail(String email);

	User findByUsername(String username);

	User findByUsernamePassword(String username, String password);

	List<User> findAll();

	List<User> findAll(int pageNumber, int pageSize);

	User create(User entity);

	User update(User entity);

	User delete(User entity);

	List<User> findUsersLikedByVideoHref(Map<String, Object> params);
}
