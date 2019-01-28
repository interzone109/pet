package ua.squirrel.user.partner.service;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.web.user.entity.User;

public interface PartnerService {
	void save(Partner partner);

	Optional<Partner> findByIdAndUser(Long id, User user);

	
	public List<Partner> findAllByUser( User user);
	
	List<Partner> saveAll( Iterable<Partner> entities);

	void delete(Partner partner);
}
