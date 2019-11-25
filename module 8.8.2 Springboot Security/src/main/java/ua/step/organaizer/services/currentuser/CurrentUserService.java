package ua.step.organaizer.services.currentuser;

import ua.step.organaizer.dto.CurrentUser;

public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}