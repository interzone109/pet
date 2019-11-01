package ua.squirrel.web.service.registration;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.web.entity.user.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>{
	UserSubscription save(UserSubscription userSubscription);

}
