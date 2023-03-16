package com.nutri.rest.mapper;

import com.nutri.rest.model.DietitianExperienceDetails;
import com.nutri.rest.model.DietitianRecognitions;
import com.nutri.rest.model.User;
import com.nutri.rest.request.CreateUserRequest;
import com.nutri.rest.request.ExperienceDetailsRequestAndResponse;
import com.nutri.rest.request.RecognitionsRequestAndResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.UserResponse;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {


    public UserResponse mapFromUserDomainToResponse(User user){
        return UserResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .price(user.getBasePrice())
                .userProfileActivated(user.getUserProfileActivated())
                .build();
    }

    public UserResponse mapFromUserDomainToResponseAlongWithProfile(User user, List<DietitianRecognitions> recognitions, List<DietitianExperienceDetails> dietitianExperienceDetails){
        UserResponse response =
                UserResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .price(user.getBasePrice())
                .userProfileActivated(user.getUserProfileActivated())
                .build();

        if(user.getProfile() !=null){
            response = UserResponse
                    .builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .userName(user.getUserName())
                    .phoneNumber(user.getPhoneNumber())
                    .price(user.getBasePrice())
                    .title(user.getProfile().getTitle())
                    .overallExperience(user.getProfile().getOverallExperience())
                    .specialistExperience(user.getProfile().getSpecialistExperience())
                    .qualifiedDegree(ItemDetailsResponse.LookupUnits.builder()
                            .unitLookupCode(user.getProfile().getQualifiedDegree().getLookupValueCode())
                            .unitLookupValue(user.getProfile().getQualifiedDegree().getLookupValue()).build())
                    .degreeUniversity(user.getProfile().getDegreeUniversity())
                    .degreeYear(user.getProfile().getDegreeYear())
                    .bio(user.getProfile().getBio())
                    .address(user.getProfile().getAddress()).build();
            if(!CollectionUtils.isEmpty(user.getProfile().getServices()))
                response.setServices(user.getProfile().getServices().stream().map(service -> ItemDetailsResponse.LookupUnits.builder()
                        .unitLookupCode(service.getLookupValueCode())
                        .unitLookupValue(service.getLookupValue()).build()).collect(Collectors.toList()));
        }

        if(!CollectionUtils.isEmpty(dietitianExperienceDetails)){
            response.setExperienceDetails(dietitianExperienceDetails.stream().map(dietitianExperienceDetails1 -> ExperienceDetailsRequestAndResponse.builder()
                    .fromYear(dietitianExperienceDetails1.getFromYear())
                    .toYear(dietitianExperienceDetails1.getToYear())
                    .organization(dietitianExperienceDetails1.getOrganization()).build()).collect(Collectors.toList()));
        }
        if(!CollectionUtils.isEmpty(recognitions)){
            response.setRecognitions(recognitions.stream().map(dietitianRecognitions1 -> RecognitionsRequestAndResponse.builder()
                    .awardsOrRecognitions(dietitianRecognitions1.getAwardsOrRecognitions())
                    .yearOfRecognition(dietitianRecognitions1.getYearOfRecognition()).build()).collect(Collectors.toList()));
        }
        if(user.getRestaurantProfile() != null){
            response.setRestaurantName(user.getRestaurantProfile().getRestaurantName());
            response.setAvgCost(user.getRestaurantProfile().getAvgCost());
            response.setCuisines(user.getRestaurantProfile().getCuisines().stream().map(lookupValue -> ItemDetailsResponse.LookupUnits.builder()
                    .unitLookupCode(lookupValue.getLookupValueCode())
                    .unitLookupValue(lookupValue.getLookupValue()).build()).collect(Collectors.toList()));
        }
        return response;
    }

    public User mapFromUserRequestToDomain(String encryptedPassword, CreateUserRequest createUserRequest){
        return User
                .builder()
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .userName(createUserRequest.getUserName())
                .password(encryptedPassword)
                .phoneNumber(createUserRequest.getPhoneNumber())
                .passwordUpdateDate(LocalDate.now())
                .build();
    }

    public UserResponse mapFromUserDomainToResponseFilteringRBSRole(User user){
        return UserResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
