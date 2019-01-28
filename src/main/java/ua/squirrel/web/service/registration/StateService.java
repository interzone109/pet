package ua.squirrel.web.service.registration;

import ua.squirrel.web.entity.user.State;

public interface StateService {
	public State findOneByName(String name);
}
