package ua.squirrel.web.registration.service;

import ua.squirrel.web.entity.user.entity.Role;

public interface RoleService {
	public Role findOneByName(String name);
}
