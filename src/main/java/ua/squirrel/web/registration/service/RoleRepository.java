package ua.squirrel.web.registration.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.web.user.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	public Role findOneByName(String name);
}
