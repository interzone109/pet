package ua.squirrel.web.registration.state.service;

import ua.squirrel.web.entity.user.State;

public interface StateService {
	public State findOneByName(String name);
}
