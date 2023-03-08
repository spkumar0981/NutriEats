package com.nutri.rest.mapper;

import com.nutri.rest.model.ExperienceDetails;
import com.nutri.rest.model.Recognitions;
import com.nutri.rest.model.User;
import com.nutri.rest.request.CreateUserRequest;
import com.nutri.rest.request.ExperienceDetailsRequestAndResponse;
import com.nutri.rest.request.RecognitionsRequestAndResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.UserResponse;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
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
                .build();
    }

    public UserResponse mapFromUserDomainToResponseAlongWithProfile(User user, List<Recognitions> recognitions, List<ExperienceDetails> experienceDetails){
        UserResponse response =
                UserResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .price(user.getBasePrice())
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

        if(!CollectionUtils.isEmpty(experienceDetails)){
            response.setExperienceDetails(experienceDetails.stream().map(experienceDetails1 -> ExperienceDetailsRequestAndResponse.builder()
                    .fromYear(experienceDetails1.getFromYear())
                    .toYear(experienceDetails1.getToYear())
                    .organization(experienceDetails1.getOrganization()).build()).collect(Collectors.toList()));
        }
        if(!CollectionUtils.isEmpty(recognitions)){
            response.setRecognitions(recognitions.stream().map(recognitions1 -> RecognitionsRequestAndResponse.builder()
                    .awardsOrRecognitions(recognitions1.getAwardsOrRecognitions())
                    .yearOfRecognition(recognitions1.getYearOfRecognition()).build()).collect(Collectors.toList()));
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
