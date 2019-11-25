package ua.step.organaizer.services.currentuser;

import org.springframework.stereotype.Service;

import ua.step.organaizer.dto.CurrentUser;
import ua.step.organaizer.enteties.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);

	@Override
	public boolean canAccessUser(CurrentUser currentUser, Long userId) {
		LOGGER.debug("Checking if user={} has access to user={}", currentUser, userId);
		return currentUser != null && (currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(userId));
	}
}