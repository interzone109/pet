package ua.step.organaizer.services.user;

import java.util.Collection;
import java.util.Optional;

import ua.step.organaizer.dto.UserCreateForm;
import ua.step.organaizer.enteties.User;

public interface UserService {
    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);
}
