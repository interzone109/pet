package ua.squirrel.user.partner.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.partner.Partner;
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
	public void deleteByIdAndUser(Partner partner, User user) {
		partnerRepository.deleteByIdAndUser(partner, user);

	}
}
