package com.nutri.rest.controller;

import com.nutri.rest.request.RatingRequest;
import com.nutri.rest.response.RatingResponse;
import com.nutri.rest.service.RatingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rating")
@Api(value = "Dietitian Controller")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get all ratings for a user")
    public List<RatingResponse> getRatings(@PathVariable String username){
        return ratingService.getRatings(username);
    }

    @GetMapping("/specificRating/{username}")
    @ApiOperation(value = "Get specific rating of a user given by another user")
    public RatingResponse getSpecificRatings(@PathVariable String username){
        return ratingService.getSpecificRatings(username);
    }

    @PostMapping
    @ApiOperation(value = "Create ratings for a user")
    public RatingResponse createRatings(@Valid @RequestBody RatingRequest ratingRequest){
        return ratingService.createRatings(ratingRequest);
    }
}
