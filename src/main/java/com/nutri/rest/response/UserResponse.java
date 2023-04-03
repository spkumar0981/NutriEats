package com.nutri.rest.response;

import com.nutri.rest.request.ExperienceDetailsRequestAndResponse;
import com.nutri.rest.request.RecognitionsRequestAndResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;

    private BigDecimal price;
    private String title;
    private int overallExperience;
    private int specialistExperience;
    private ItemDetailsResponse.LookupUnits qualifiedDegree;

    private String degreeUniversity;
    private Long degreeYear;
    private String bio;
    private String address;

    private List<ItemDetailsResponse.LookupUnits> services;

    private List<RecognitionsRequestAndResponse> recognitions;

    private List<ExperienceDetailsRequestAndResponse> experienceDetails;
    private int avgCost;
    private List<ItemDetailsResponse.LookupUnits> cuisines;

    private String restaurantName;

    private String userProfileActivated;

    private String restaurantImage;
}
