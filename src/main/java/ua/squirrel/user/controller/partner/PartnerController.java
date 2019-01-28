package ua.squirrel.user.controller.partner;

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
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.partner.PartnerModel;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.service.partner.PartnerServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

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

		return getPartnerModel(id, userCurrentSesion);
	}

	@PutMapping
	public PartnerModel updatePartnerInfo(@PathVariable("partner_id") Long id, @RequestBody PartnerModel partnerModel,
			Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update curent partners");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		Partner currentPartner = getCurrentPartner(id, userCurrentSesion);

		currentPartner.setPartnerMail(partnerModel.getPartnerMail());
		currentPartner.setPhonNumber(partnerModel.getPhonNumber());
		currentPartner.setCompany(partnerModel.getCompany());
		partnerServiceImpl.save(currentPartner);

		return getPartnerModel(currentPartner.getId(), userCurrentSesion);
	}

	@DeleteMapping
	public void getDeletePartner(@PathVariable("partner_id") Long id, Authentication authentication)
			throws NotFoundException {
		log.info("LOGGER: delete curent partners");

		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		partnerServiceImpl.delete(getCurrentPartner(id, userCurrentSesion));
		
	}

	
	private Partner getCurrentPartner(Long id, User currentUser) throws NotFoundException {
		return partnerServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Partner not found"));
	}

	private PartnerModel getPartnerModel(Long id, User user) throws NotFoundException {
		Partner partner = getCurrentPartner(id, user);

		List<ProductModel> productsModel = new ArrayList<>();

		partner.getProducts().stream().forEach(obj -> {
			productsModel.add(ProductModel.builder().id(obj.getId()).name(obj.getName())
					.description(obj.getDescription()).group(obj.getGroup())
					.propertiesProduct(obj.getPropertiesProduct()).measureProduct(obj.getMeasureProduct()).build());
		});

		return PartnerModel.builder().id(partner.getId()).company(partner.getCompany())
				.partnerMail(partner.getPartnerMail()).phonNumber(partner.getPhonNumber()).productsModel(productsModel)
				.build();
	}

	/*
	  тестовый джесон 
	  
	  { "company": "куриные братишки eeee",
	   "phonNumber":"8-800-555-444",
	    "partnerMail": "curina@potatomail.com" 
	   }
	 */

}
