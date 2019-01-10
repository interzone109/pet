package ua.squirrel.web.registration.role.service;

import ua.squirrel.entity.user.Role;

public interface RoleService {
	public Role findOneByName(String name);
}
