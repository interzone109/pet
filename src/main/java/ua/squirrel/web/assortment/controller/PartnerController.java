package ua.squirrel.web.assortment.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.assortment.partner.Partner;
import ua.squirrel.user.assortment.partner.service.PartnerServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.user.service.UserServiceImpl;

@RestController
@RequestMapping("/partner")
@Slf4j
public class PartnerController {
	
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@PostMapping
	public List <Partner> addNewPartner(@RequestBody List <Partner> newPartner) {
		log.info("LOGGER: add new partners " );
		
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		newPartner.stream().forEach(obj->{
			partnerServiceImpl.save(obj);
		});
		
		newPartner.stream().forEach(obj->{
			user.getPartners().add(obj);
		});
		
		userServiceImpl.save(user);
		
		return  userServiceImpl.findByIdAndFetchPartnersEagerly(user.getId()).getPartners();
		}
	
	
	
	@GetMapping
	public List<Partner> getSavePartner() {
		log.info("LOGGER: show all partners");
		
		User user = userServiceImpl.findOneByLogin("test1").get();

		return userServiceImpl.findByIdAndFetchPartnersEagerly(user.getId()).getPartners();
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
