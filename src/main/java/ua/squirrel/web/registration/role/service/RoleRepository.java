package ua.squirrel.web.registration.role.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.web.entity.user.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	public Role findOneByName(String name);
}
