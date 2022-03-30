package edu.vn.service;

import java.util.List;

import edu.vn.dto.UserDto;
import edu.vn.entity.User;

public interface UserService {

	User findById(Integer id);

	User finByemail(String email);

	User findByUsername(String username);

	User login(String username, String password);

	User resetPassword(String email);

	List<User> findAll();

	List<User> findAll(int pageNumber, int pageSize);

	User create(String username, String password, String email);

	User update(User entity);

	User delete(String username);

	List<UserDto> findUsersLikedVideoByVideoHref(String href);
}
