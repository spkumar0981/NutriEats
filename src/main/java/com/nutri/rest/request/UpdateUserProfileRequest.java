package com.nutri.rest.request;

import com.nutri.rest.response.ItemDetailsResponse;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

@Data
public class UpdateUserProfileRequest {

    @Email
    private String userName;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String price;
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

    private String restaurantImage;

}
