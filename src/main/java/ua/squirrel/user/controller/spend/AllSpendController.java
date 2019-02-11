package ua.squirrel.user.controller.spend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.spending.SpendModel;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.spending.SpendServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/spends")
@Slf4j
//@Secured("USER")
public class AllSpendController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private SpendServiceImpl spendServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	/**
	 * метод
	 * 
	 */
	@GetMapping
	public List<SpendModel> getAllSpends(Authentication authentication) {
		log.info("LOGGER: show all spends");

		User user = userServiceImpl.findOneByLogin("test1").get();

		return getAllSpendModel(user);
	}

	private List<SpendModel> getAllSpendModel(User user) {
		List<SpendModel> spendModels = new ArrayList<>();
		
		spendServiceImpl.findAllByUserOrderByDateAsc(user).forEach(spend->{
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

}
