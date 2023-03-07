package com.nutri.rest.service;

import com.nutri.rest.mapper.RatingMapper;
import com.nutri.rest.model.LookupValue;
import com.nutri.rest.model.Rating;
import com.nutri.rest.model.User;
import com.nutri.rest.repository.LookupRepository;
import com.nutri.rest.repository.RatingRepository;
import com.nutri.rest.repository.UserRepository;
import com.nutri.rest.request.RatingRequest;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.RatingResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final LookupRepository lookupRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository, LookupRepository lookupRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.lookupRepository = lookupRepository;
    }

    public List<RatingResponse> getRatings(String username){
        User user = userRepository.findByUserName(username).get();
        return ratingRepository.findByToUserId(user).stream().map(RatingMapper::mapToRatingResponse).collect(Collectors.toList());
    }

    public RatingResponse getSpecificRatings(String username){
        User fromUser = userRepository.findByUserName(CurrentUserService.getLoggedUserName()).get();
        User toUser = userRepository.findByUserName(username).get();
        Rating rating = ratingRepository.findByFromUserIdAndToUserId(fromUser, toUser);
        if(rating!=null)
            return RatingMapper.mapToRatingResponse(rating);
        else
           return null;
    }

    public RatingResponse createRatings(RatingRequest ratingRequest){
        User fromUser = userRepository.findByUserName(CurrentUserService.getLoggedUserName()).get();
        User toUser = userRepository.findByUserName(ratingRequest.getUsername()).get();
        LookupValue lookupValue = lookupRepository.findByLookupValueCode(ratingRequest.getCommentCategory().getUnitLookupCode());
        List<LookupValue> lookupValues = ratingRequest.getCommentOptions().stream().map(lookupUnits ->
                lookupRepository.findByLookupValueCode(lookupUnits.getUnitLookupCode())).collect(Collectors.toList());
        Rating rating = ratingRepository.findByFromUserIdAndToUserId(fromUser, toUser);
        if(rating == null) {
            rating = Rating.builder()
                    .fromUserId(fromUser)
                    .toUserId(toUser)
                    .rating(ratingRequest.getRating())
                    .commentCategory(lookupValue)
                    .comments(ratingRequest.getComments())
                    .recommended(ratingRequest.getRecommended())
                    .commentOptions(lookupValues).build();
        }else {
            rating.setRating(ratingRequest.getRating());
            rating.setComments(ratingRequest.getComments());
            rating.setRecommended(ratingRequest.getRecommended());
            rating.setCommentOptions(lookupValues);
            rating.setCommentCategory(lookupValue);
        }
        return RatingMapper.mapToRatingResponse(ratingRepository.save(rating));
    }
}
