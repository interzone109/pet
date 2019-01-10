package ua.squirrel.web.registration.state.service;

import ua.squirrel.entity.user.State;

public interface StateService {
	public State findOneByName(String name);
}
