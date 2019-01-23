package ua.squirrel.web.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.web.user.entity.Role;

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
