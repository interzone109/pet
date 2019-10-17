package ua.squirrel.web.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.web.entity.user.UserSubscription;
@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;
	
	@Override
	public UserSubscription save(UserSubscription userSubscription) {
		// TODO Auto-generated method stub
		return userSubscriptionRepository.save(userSubscription);
	}
	
}
