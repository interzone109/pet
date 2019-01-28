package ua.squirrel.web.service.registration;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.web.entity.user.State;

public interface StateRepository extends JpaRepository<State, Long>{

	public State findOneByName(String name);

}
