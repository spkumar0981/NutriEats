package com.nutri.rest.mapper;

import com.nutri.rest.model.Rating;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.RatingResponse;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class RatingMapper {
    public RatingResponse mapToRatingResponse(Rating rating){
        return RatingResponse
                .builder()
                .username(rating.getFromUserId().getUserName())
                .fullName(rating.getFromUserId().getFirstName()+", "+rating.getFromUserId().getLastName())
                .rating(rating.getRating())
                .commentCategory(LookupMapper.mapToLookups(rating.getCommentCategory()))
                .comments(rating.getComments())
                .recommended(rating.getRecommended())
                .commentOptions(rating.getCommentOptions().stream().map(option -> LookupMapper.mapToLookups(option)).collect(Collectors.toList()))
                .build();
    }
}
