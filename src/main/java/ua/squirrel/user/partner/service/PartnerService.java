package ua.squirrel.user.partner.service;

import java.util.Optional;

import ua.squirrel.user.partner.Partner;
import ua.squirrel.web.entity.user.User;

public interface PartnerService {
	void save(Partner partner);

	Optional<Partner> findByIdAndUser(Long id, User user);

	void deleteByIdAndUser(Partner partner, User user);

}
