package ua.squirrel.user.controller.updates;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.user.service.store.spending.SpendServiceImpl; 

@Controller
@Slf4j
public class AutoUpdateController {
	

	@Autowired
	private SpendServiceImpl spendServiceImpl;
	
	
	@Scheduled(fixedRate = 500000)
    public void reportCurrentTime() {
        log.info("System controller");
    }
	/**
	 * методраз в сутки пересчитывает итоговую сумму расходов
	 * 
	 * */
	@Scheduled(fixedRate = 50000)
    public void checkSpebdsRecount() {
        log.info("auto recound all open spends ");
        LocalDate today = LocalDate.now();
    	List<Spend> allSpends =  spendServiceImpl.findAllByIsOpen(true);
    	allSpends.forEach(spend->{
    		LocalDate lastPayDay = spend.getLasteDate();
    		LocalDate nextPayDay ;
    		int interval = spend.getInterval();
    		 if(interval<0){
    			 interval = Math.abs(interval);
    			 nextPayDay = lastPayDay.plusMonths(interval);
    			}else {
    			 nextPayDay = lastPayDay.plusDays(interval);
    			}
    		if(nextPayDay.isAfter(today)) {
    			int total = spend.getStep() + spend.getCost();
    			 spend.setCost(total);
    			 spend.setLasteDate(nextPayDay);
    		}
    		 
    	 });
    	spendServiceImpl.saveAll(allSpends);
    }
	
	


}
