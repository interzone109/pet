package ua.squirrel.user.service.partner;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.web.entity.user.User;

@Service
public class PartnerServiceImpl implements PartnerService {

	private PartnerRepository partnerRepository;

	@Autowired
	public PartnerServiceImpl(PartnerRepository partnerRepository) {
		this.partnerRepository = partnerRepository;
	}

	@Override
	public void save(Partner partner) {
		partnerRepository.save(partner);

	}

	@Override
	public Optional<Partner> findByIdAndUser(Long id, User user) {
		return partnerRepository.findByIdAndUser(id, user);
	}

	@Override
	public void delete(Partner partner) {
		partnerRepository.delete(partner);

	}
	
	@Override
	public List<Partner> findAllByUser( User user) {
		return partnerRepository.findAllByUser( user);
	}
	
	public List<Partner> saveAll( Iterable<Partner> entities) {
		return partnerRepository.saveAll( entities);
	}
}
