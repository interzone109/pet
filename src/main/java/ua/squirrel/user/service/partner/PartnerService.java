package ua.squirrel.user.service.partner;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.web.entity.user.User;

public interface PartnerService {
	void save(Partner partner);

	Optional<Partner> findByIdAndUser(Long id, User user);

	
	public List<Partner> findAllByUser( User user);
	
	List<Partner> saveAll( Iterable<Partner> entities);

	void delete(Partner partner);
}
