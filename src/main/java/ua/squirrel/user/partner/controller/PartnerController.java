package ua.squirrel.user.partner.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import ua.squirrel.user.product.ProductModel;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.user.service.UserServiceImpl;

@RestController
@RequestMapping("/partners/{partner_id}/info")
@Slf4j
public class PartnerController {
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;

	
	

	/**
	 * метод находит по id и текущему User партнера и возращает информацию о нем и о
	 * его товаре
	 */
	@GetMapping
	public PartnerModel getPartnerInfo(Authentication authentication, @PathVariable("partner_id") Long id)
			throws NotFoundException {

		log.info("LOGGER: return curent partner product");

		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		Partner partner = getCurrentPartner(id, userCurrentSesion);

		List<ProductModel> productsModel = new ArrayList<>();

		partner.getProducts().stream().forEach(obj -> {
			productsModel.add(ProductModel.builder()
					.id(obj.getId())
					.name(obj.getName())
					.description(obj.getDescription())
					.price(obj.getPrice())
					.groupProduct(obj.getGroupProduct())
					.measureProduct(obj.getMeasureProduct()).build());
		});

		return PartnerModel.builder().id(partner.getId()).company(partner.getCompany())
				.partnerMail(partner.getPartnerMail()).phonNumber(partner.getPhonNumber()).productsModel(productsModel)
				.build();
	}

	@PutMapping
	public Partner updatePartnerInfo(@PathVariable("partner_id") Long id, @RequestBody PartnerModel partnerModel,
			Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update curent partners");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		Partner currentPartner = getCurrentPartner(id, userCurrentSesion);

		currentPartner.setPartnerMail(partnerModel.getPartnerMail());
		currentPartner.setPhonNumber(partnerModel.getPhonNumber());
		currentPartner.setCompany(partnerModel.getCompany());
		partnerServiceImpl.save(currentPartner);

		return currentPartner;
	}

	@DeleteMapping
	public void getDeletePartner(@PathVariable("partner_id") Long id, Authentication authentication)
			throws NotFoundException {
		log.info("LOGGER: delete curent partners");

		// User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
	}

	private Partner getCurrentPartner(Long id , User currentUser) throws NotFoundException {
		return partnerServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Partner not found"));
	}

}


