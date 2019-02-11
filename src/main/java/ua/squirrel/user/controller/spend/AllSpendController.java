package ua.squirrel.user.controller.spend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.user.entity.store.spending.SpendModel;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.spending.SpendServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@Slf4j
@RequestMapping("/spends")
//@Secured("USER")
public class AllSpendController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private SpendServiceImpl spendServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	
	/**
	 * метод возращает все затраты 
	 * 
	 */
	@GetMapping
	public List<SpendModel> getAllSpends(Authentication authentication) {
		log.info("LOGGER: show all spends");

		User user = userServiceImpl.findOneByLogin("test1").get();

		return getAllSpendModel(spendServiceImpl.findAllByUserOrderByDateAsc(user));
	}
	/**
	 * метод возращает все затраты между двумя датами
	 * @throws NotFoundException 
	 * 
	 */
	
	@PostMapping
	public List<SpendModel> createSpends(Authentication authentication, @RequestBody SpendModel spendModel) throws NotFoundException {
		log.info("LOGGER: save new spends");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		Spend spend = new Spend();
		spend.setName(spendModel.getName());
		spend.setCost(spendModel.getCost());
		spend.setDescription(spendModel.getDescription());
		spend.setDate(spendModel.getDate());
		spend.setInterval(spendModel.getInterval());
		spend.setIsRegular(spendModel.isRegular());
		spend.setUser(user);
		spend.setStore(storeServiceImpl.findOneByIdAndUser(spendModel.getStorId(), user).get());
		
		spendServiceImpl.save(spend);

		return getAllSpendModel(spendServiceImpl.findAllByUserOrderByDateAsc(user));
	}

	private List<SpendModel> getAllSpendModel(List<Spend> spends) {
		List<SpendModel> spendModels = new ArrayList<>();
		
		spends.forEach(spend->{
			spendModels.add(SpendModel.builder()
					.id(spend.getId())
					.name(spend.getName())
					.cost(spend.getCost())
					.interval(spend.getInterval())
					.isRegular(spend.getIsRegular())
					.date(spend.getDate())
					.storId(spend.getStore().getId()).build());
		});
		return spendModels;
	}

	/*
	 {
      "name": "аренда ",
      "description": "test",
      "cost": 10000,
      "interval": 15,
      "date": "2019-05-15T22:00:00.000+0000",
      "storId": 1,
      "regular": true
   }
	 * */
	
	
}
