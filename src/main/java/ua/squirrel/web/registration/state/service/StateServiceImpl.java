package ua.squirrel.web.registration.state.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateServiceImpl implements StateService{

	private final StateRepository stateRepository;

	@Autowired
	public  StateServiceImpl(StateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}
	
}
