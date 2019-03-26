package ua.squirrel.user.controller.partner;


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
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.partner.PartnerModel;
import ua.squirrel.user.service.partner.PartnerServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;
/**
 * Controller :
 * GET return partners list 
 * POST create new partner, save in db  and return new partner model
 * 
 * */
@RestController
@RequestMapping("/user/partners")
@Slf4j
public class AllPartnersController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	
	/**
	 * метод получает зарегисрированого пользователя 
	 * берет у него список партнеров и перезаписывает их
	 * в List<PartnerModel>, после чего возращает данные
	 * 
	 * */
	@GetMapping
	public List<PartnerModel> getAllPartner(Authentication authentication) {
		log.info("LOGGER: retгrn all partners for current user: /user/partners");
		
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		List<PartnerModel> partnersModel = new ArrayList<>();
		partnerServiceImpl.findAllByUser(user).stream().forEach(obj->{
			partnersModel.add(PartnerModel.builder()
					.id(obj.getId())
					.company(obj.getCompany())
					.partnerMail(obj.getPartnerMail())
					.phonNumber(obj.getPhonNumber()).build());
		});
		
		return partnersModel;
	}
	
	
	
	/**
	 * метод получает модель newPartners,
	 * после чего переписывает данные из поделей в сущности и сохраняет их в базу
	 * */
	@PostMapping
	public PartnerModel addNewPartner(@RequestBody PartnerModel newPartners ,Authentication authentication) {
		log.info("LOGGER: save new partners from model: /user/partners" );
		User user = userServiceImpl.findOneByLogin("test1").get();

 			Partner addPartner = new Partner();
			addPartner.setCompany(newPartners.getCompany());
			addPartner.setPartnerMail(newPartners.getPartnerMail());
			addPartner.setPhonNumber(newPartners.getPhonNumber());
			addPartner.setUser(user);
		
		partnerServiceImpl.save(addPartner);
		
		newPartners.setId(addPartner.getId());

		return  newPartners;
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
