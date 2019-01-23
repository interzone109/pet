package ua.squirrel.web.registration.service;

import ua.squirrel.web.user.entity.Role;

public interface RoleService {
	public Role findOneByName(String name);
}
