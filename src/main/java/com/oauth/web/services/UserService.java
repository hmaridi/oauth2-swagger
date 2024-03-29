package com.oauth.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oauth.web.entities.User;
import com.oauth.web.repositories.UserRepositroy;

@Service
public class UserService {

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserRepositroy userRepositroy;

	public User fetchByUnameAndActivationKey(String username, String activationKey) {
		return userRepositroy.findByUsernameAndActivationKey(username, activationKey);

	}

	public User saveUser(User userDetails) {
		userDetails.setPassword(getPasswordEncoder().encode(userDetails.getPassword()));
		return userRepositroy.save(userDetails);
	}

	public User savePassword(User userDetails) {
		userDetails.setPassword(getPasswordEncoder().encode(userDetails.getPassword()));
		return userRepositroy.save(userDetails);
	}

	public User savenopwd(User user) {
		return userRepositroy.save(user);
	}

	public User fetchByName(final String username) {
		return userRepositroy.findByUsername(username);
	}

	public Iterable<User> listOfUserDetails() {
		return userRepositroy.findAll();
	}

	public User fetchById(final Long id) {
		return userRepositroy.findOne(id);
	}

	public void deleteById(final Long id) {
		userRepositroy.delete(id);
	}

}
