package com.nutri.rest.response;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {

    private String username;

    private String fullName;

    private Long rating;

    private ItemDetailsResponse.LookupUnits commentCategory; //consulted for ??
    private List<ItemDetailsResponse.LookupUnits> commentOptions; //happyOrSadWith for ??

    private String comments;

    private Boolean recommended;
}
