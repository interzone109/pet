package ua.squirrel.user.controller.spend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.user.entity.store.spending.SpendModel;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.spending.SpendServiceImpl;
import ua.squirrel.user.utils.SmallOneUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.account.AccountAppServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@Slf4j
@RequestMapping("/user/spends/data")
//@Secured("USER")
public class AllSpendController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private SpendServiceImpl spendServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private SmallOneUtil smallOneUtil;
	@Autowired
	private AccountAppServiceImpl accountAppServiceImpl;
	
	
	/**
	 * метод возращает все затраты между двумя датами
	 * @throws NotFoundException 
	 * 
	 */
	@PostMapping("/find")
	public List<SpendModel> findSpends(Authentication authentication, @RequestBody SpendModel spendModel) throws NotFoundException {
		log.info("LOGGER: find spends");
	
		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();
		
		
		 if(spendModel.getStoreId() == 0) {
			return getAllSpendModel(spendServiceImpl.findAllByUserAndLasteDateBetweenOrderByLasteDateAsc(user,
					smallOneUtil.convertDate(spendModel.getDateStart()),
					smallOneUtil.convertDate(spendModel.getDateEnd())));
		}else{
			Store store = getCurrentStore(user , spendModel.getStoreId());
			return getAllSpendModel(spendServiceImpl.findAllByStoreAndLasteDateBetweenOrderByLasteDateAsc( store,
					smallOneUtil.convertDate(spendModel.getDateStart()),
					smallOneUtil.convertDate(spendModel.getDateEnd())));
			
		}
	}
	
	@PostMapping
	public SpendModel createSpends(Authentication authentication, @RequestBody SpendModel spendModel) throws NotFoundException {
		log.info("LOGGER: save new spends");
		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();
		Store store = null ;
		if(spendModel.getStoreId() != 0) {
			store = storeServiceImpl.findOneByIdAndUser(spendModel.getStoreId(), user).get();
			
		}
		LocalDate date = smallOneUtil.convertDate(spendModel.getDateStart());
		Spend spend = new Spend();
		spend.setName(spendModel.getName());
		spend.setCost(spendModel.getCost());
		spend.setStep(spendModel.getStep());
		spend.setDate(date);
		spend.setLasteDate(date);
		spend.setInterval(spendModel.getInterval());
		spend.setIsOpen(true);
		spend.setUser(user);
		spend.setStore(store);
		
		
		
		return buildModel(spendServiceImpl.save(spend));
	}

	
	@DeleteMapping("{spend_id}")
	public void deleteSpends(Authentication authentication, @PathVariable("spend_id") Long spendId ) throws NotFoundException {
		log.info("LOGGER: delete spends");
		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();
		Spend spend = spendServiceImpl.findOneByUserAndId(user, spendId).get();
		spendServiceImpl.remove(spend);
	}
	
	
	
	private List<SpendModel> getAllSpendModel(List<Spend> spends) {
		List<SpendModel> spendModels = new ArrayList<>();
		
		spends.forEach(spend->{
			spendModels.add(buildModel(spend));
		});
		return spendModels;
	}
	
	private SpendModel buildModel(Spend spend) {
		String addres= (spend.getStore() == null )?" ":spend.getStore().getAddress();
		long storeId = 	(spend.getStore() == null )?0:spend.getStore().getId();
		return SpendModel.builder()
				.id(spend.getId())
				.name(spend.getName())
				.cost(spend.getCost())
				.step(spend.getStep())
				.interval(spend.getInterval())
				.isOpen(spend.getIsOpen())
				.dateStart(spend.getDate().toString())
				.dateEnd(spend.getLasteDate().toString())
				.storeId(storeId)
				.storeName(addres)
				.build();
		
	}
	
	private Store getCurrentStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found id - "+ id));
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
