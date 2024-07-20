package org.example.courzelo.serviceImpls;

import org.example.courzelo.models.Rating;

public interface IRatingService {
    Rating addRatingToTicket(String ticketId, Rating rating);
    Rating updateRating(String ratingId, Rating rating);
    Rating getRatingById(String ratingId);
    void deleteRating(String ratingId);
}
