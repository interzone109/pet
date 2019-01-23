package ua.squirrel.user.partner.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.web.user.entity.User;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

	Optional<Partner> findOneByUser(User user);

	Optional<Partner> findByIdAndUser(Long id, User user);

	void deleteByIdAndUser(Partner partner, User user);

	
}
