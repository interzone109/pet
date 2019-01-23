package ua.squirrel.user.partner.controller;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.user.partner.entity.PartnerModel;
import ua.squirrel.web.entity.user.entity.User;
import ua.squirrel.web.registration.user.service.UserServiceImpl;

@RestController
@RequestMapping("/partners")
@Slf4j
public class AllPartnersController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	
	/**
	 * метод получает список новых партнеров List <PartnerModel> newPartners,
	 * после чего переписывает данные из поделей в сущности и сохраняет их в базу
	 * */
	@PostMapping
	public List <PartnerModel> addNewPartner(@RequestBody List <PartnerModel> newPartners ,Authentication authentication) {
		log.info("LOGGER: save new partners from model " );
		
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		List<Partner> userPartnersList = user.getPartners();
		newPartners.stream().forEach(obj->{
			Partner addPartner = new Partner();
			addPartner.setCompany(obj.getCompany());
			addPartner.setPartnerMail(obj.getPartnerMail());
			addPartner.setPhonNumber(obj.getPhonNumber());
			addPartner.setUser(user);
			userPartnersList.add(addPartner); 
		});
		
		userServiceImpl.save(user);

		return  newPartners;
		}
	
	
	/**
	 * метод получает зарегисрированого пользователя 
	 * берет у него список партнеров и перезаписывает их
	 * в List<PartnerModel>, после чего возращает данные
	 * 
	 * */
	@GetMapping
	public List<PartnerModel> getAllPartner(Authentication authentication) {
		log.info("LOGGER: show all partners ");
		
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		List<Partner> partners = user.getPartners();
		
		List<PartnerModel> partnersModel = new ArrayList<>();
		
		partners.stream().forEach(obj->{
			partnersModel.add(PartnerModel.builder()
					.id(obj.getId())
					.company(obj.getCompany())
					.partnerMail(obj.getPartnerMail())
					.phonNumber(obj.getPhonNumber()).build());
		});

		return partnersModel;
	}
	/*тестовый джесон
[
   {
      "company": "куриные братишки",
      "phonNumber": "8-800-555-444",
      "partnerMail": "curina@potatomail.com"
   },
   {
      "company": "картафяные братишки",
      "phonNumber": "8-800-555-555",
      "partnerMail": "potate@potatomail.com"
   }
]
	 */
}
