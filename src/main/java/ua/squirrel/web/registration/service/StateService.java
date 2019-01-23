package ua.squirrel.web.registration.service;

import ua.squirrel.web.user.entity.State;

public interface StateService {
	public State findOneByName(String name);
}
