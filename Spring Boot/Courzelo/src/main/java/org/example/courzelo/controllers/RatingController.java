package org.example.courzelo.controllers;


import org.example.courzelo.models.Rating;

import org.example.courzelo.services.RatingServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ratings")
public class RatingController {

    @Autowired
    private RatingServiceImp ratingService;

    @PostMapping("/{ticketId}")
    public Rating addRatingToTicket(@PathVariable String ticketId, @RequestBody Rating rating) {
        return ratingService.addRatingToTicket(ticketId, rating);
    }

    @PutMapping("/{ratingId}")
    public Rating updateRating(@PathVariable String ratingId, @RequestBody Rating rating) {
        return ratingService.updateRating(ratingId, rating);
    }


    @GetMapping("/{ratingId}")
    public Rating getRatingById(@PathVariable String ratingId) {
        return ratingService.getRatingById(ratingId);
    }

    @GetMapping("/all")
    public List<Rating> getRatings() {
        return ratingService.getAllRating();
    }

    @DeleteMapping("/{ratingId}")
    public void deleteRating(@PathVariable String ratingId) {
        ratingService.deleteRating(ratingId);
    }
}

