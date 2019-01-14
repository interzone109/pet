package ua.squirrel.user.assortment.partner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.squirrel.user.assortment.partner.Partner;

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

}
