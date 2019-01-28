package ua.squirrel.web.service.registration;

import ua.squirrel.web.entity.user.Role;

public interface RoleService {
	public Role findOneByName(String name);
}
