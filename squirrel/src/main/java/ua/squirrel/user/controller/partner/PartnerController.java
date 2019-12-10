package ua.squirrel.user.controller.partner;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import ua.squirrel.web.service.account.AccountAppServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;



/**
 * Controller :
 * GET return partner and list of his product 
 * PUT update info about partner
 * 
 * */

@RestController
@RequestMapping("/user/partners/{partner_id}/info")
@Slf4j
public class PartnerController {
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private AccountAppServiceImpl accountAppServiceImpl;

	/**
	 * метод находит по id и текущему User партнера и возращает информацию о нем и о
	 * его товаре
	 */
	@GetMapping
	public PartnerModel getPartnerInfo(Authentication authentication, @PathVariable("partner_id") Long id)
			throws NotFoundException {

		log.info("LOGGER: return current partner product: /user/partners/{partner_id}/info ");

		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();
		// вызываем медот преобразования сущности в модель
		return getPartnerModel(id, user);
	}

	@PutMapping
	public PartnerModel updatePartnerInfo(@PathVariable("partner_id") Long id, @RequestBody PartnerModel partnerModel,
			Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update curent partners info : /user/partners/{partner_id}/info");
		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();
		// получаем из базы Partner по ид и текущему пользователю
		Partner currentPartner = getCurrentPartner(id, user);
		//обновляем данные и сохраняем в базу
		currentPartner.setPartnerMail(partnerModel.getPartnerMail());
		currentPartner.setPhonNumber(partnerModel.getPhonNumber());
		currentPartner.setCompany(partnerModel.getCompany());
		partnerServiceImpl.save(currentPartner);
		// возращаем обновленные данные без списка продуктов
		return PartnerModel.builder()
				.id(currentPartner.getId())
				.company(currentPartner.getCompany())
				.partnerMail(currentPartner.getPartnerMail())
				.phonNumber(currentPartner.getPhonNumber())
				.build();
	}

	
	private Partner getCurrentPartner(Long id, User currentUser) throws NotFoundException {
		return partnerServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Partner not found"));
	}

	private PartnerModel getPartnerModel(Long id, User user) throws NotFoundException {
		// получаем из базы Partner по ид и текущему пользователю
		Partner partner = getCurrentPartner(id, user);
		// создаем список моделей  ProductModel 
		List<ProductModel> productsModel = new ArrayList<>();
		// заполняем список данными партнера
		
		partner.getProducts().stream().forEach(obj -> {
			productsModel.add(ProductModel.builder()
					.id(obj.getId())
					.name(obj.getName())
					.description(obj.getDescription())
					.group(obj.getGroup())
					.propertiesProduct(obj.getPropertiesProduct().getName())
					.measureProduct(obj.getMeasureProduct().getMeasure()).build());
		});
		
		// строим и возращаем PartnerModel 
		return PartnerModel.builder()
				.id(partner.getId())
				.company(partner.getCompany())
				.partnerMail(partner.getPartnerMail())
				.phonNumber(partner.getPhonNumber())
				.productsModel(productsModel)
				.build();
	}

	/*
	 * тестовый джесон
	 * 
	 * { "company": "куриные братишки eeee", "phonNumber":"8-800-555-444",
	 * "partnerMail": "curina@potatomail.com" }
	 */

}
