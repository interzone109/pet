package ua.squirrel.web.registration.service;

import ua.squirrel.web.entity.user.entity.State;

public interface StateService {
	public State findOneByName(String name);
}
