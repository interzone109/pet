package ua.squirrel.user.partner.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.partner.Partner;
import ua.squirrel.user.partner.PartnerModel;
import ua.squirrel.user.partner.service.PartnerServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.user.service.UserServiceImpl;

@RestController
@RequestMapping("/partners/{id}/info")
@Slf4j
public class PartnerController {
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	@GetMapping
	public Partner getPartnerInfo(@PathVariable("id") Long id ) throws NotFoundException {
		log.info("LOGGER: show curent partners");
		
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		return getPartner(id, userCurrentSesion);
	}
	
	@DeleteMapping
	public void getDeletePartner(@PathVariable("id") Long id ) throws NotFoundException {
		log.info("LOGGER: delete curent partners");
		
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();


	}
	
	@PutMapping
	public Partner updatePartnerInfo(@PathVariable("id") Long id , 
			@RequestBody PartnerModel partnerModel) throws NotFoundException  {
		log.info("LOGGER: update curent partners");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		
		Partner currentPartner = getPartner(id ,userCurrentSesion );
		
		currentPartner.setPartnerMail(partnerModel.getPartnerMail());
		currentPartner.setPhonNumber(partnerModel.getPhonNumber());
		currentPartner.setCompany(partnerModel.getCompany());
		partnerServiceImpl.save(currentPartner);
		
		return currentPartner; 
	}
	
	
	private Partner getPartner(Long id,User user) throws NotFoundException {
		return	partnerServiceImpl.findByIdAndUser(id, user).
				orElseThrow( () ->new NotFoundException("Partner not found"));
	}
}
