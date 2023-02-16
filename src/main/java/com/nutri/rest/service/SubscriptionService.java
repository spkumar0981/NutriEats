package com.nutri.rest.service;

import com.nutri.rest.mapper.DietitianMapper;
import com.nutri.rest.model.LookupValue;
import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.repository.LookupRepository;
import com.nutri.rest.repository.SubscriptionRepository;
import com.nutri.rest.repository.UserRepository;
import com.nutri.rest.request.DietitianRequest;
import com.nutri.rest.response.DietitansListResponse;
import com.nutri.rest.utils.SubscriptionStatus;
import com.nutri.rest.utils.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    private final LookupRepository lookupRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, LookupRepository lookupRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.lookupRepository = lookupRepository;
    }

    public Page<DietitansListResponse> getAllDietitians(Pageable pageable) {
        User customer = getCurrentLoggedUserDetails();
        return userRepository.findByUserType(UserRoles.ROLE_DIETITIAN.name(), pageable).map(dietitian -> {
            Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
            return DietitianMapper.mapFromUserDomainToResponse(dietitian, subscription);
        });
    }

    public String hireDietitian(DietitianRequest dietitianRequest) {
        User customer = getCurrentLoggedUserDetails();
        User dietitian = userRepository.findByUserName(dietitianRequest.getUserName()).get();
        LookupValue subscribedStatus = lookupRepository.findByLookupValueCode(SubscriptionStatus.SUBSCRIPTION_STATUS_1.name());
        Subscription subscription = Subscription.builder()
                .customerId(customer)
                .dietitianId(dietitian)
                .status(subscribedStatus)
                .customerInput(dietitianRequest.getCustomerInput())
                .build();
        subscriptionRepository.save(subscription);
        return "Dietitian hired successfully";
    }

    private User getCurrentLoggedUserDetails(){
        String loggedUserName = CurrentUserService.getLoggedUserName();
        return userRepository.findByUserName(loggedUserName).get();
    }
}
