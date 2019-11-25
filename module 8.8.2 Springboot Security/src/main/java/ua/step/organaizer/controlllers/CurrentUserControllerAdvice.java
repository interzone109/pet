package ua.step.organaizer.controlllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ua.step.organaizer.dto.CurrentUser;

@ControllerAdvice
public class CurrentUserControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserControllerAdvice.class);

    @ModelAttribute("currentUser")
    public CurrentUser getCurrentUser(Authentication authentication) {
    	if (authentication != null)
    	{
        	LOGGER.info("get user {}", authentication.getName());    		
    	}
        return (authentication == null) ? null : (CurrentUser) authentication.getPrincipal();
    }
}