package com.nutri.rest.request;

import com.nutri.rest.response.ItemDetailsResponse;
import lombok.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {

    @NotEmpty(message = "username is mandatory")
    private String username;

    @NonNull
    private Long rating;

    private ItemDetailsResponse.LookupUnits commentCategory; //consulted for ??

    private List<ItemDetailsResponse.LookupUnits> commentOptions; //happyOrSadWith for ??

    private String comments;

    private Boolean recommended;
}
