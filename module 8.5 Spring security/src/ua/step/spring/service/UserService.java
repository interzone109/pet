package ua.step.spring.service;

import ua.step.spring.entity.User;

/**
 * 
 */
public interface UserService {
	User getUser(String login);
}