package ua.squirrel.web.registration.state.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.squirrel.entity.user.State;

public interface StateRepository extends JpaRepository<State, Long>{

	public State findOneByName(String name);

}
