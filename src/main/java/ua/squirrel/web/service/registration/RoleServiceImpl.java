package ua.squirrel.web.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.web.entity.user.Role;

@Service
public class RoleServiceImpl implements RoleService{

	private final RoleRepository roleRepository;

	@Autowired
	public  RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public Role findOneByName(String name) {
		return roleRepository.findOneByName(name);
	}
}
