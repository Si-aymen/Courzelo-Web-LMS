package org.example.courzelo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Rating;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.repositories.RatingRepository;
import org.example.courzelo.repositories.TicketRepository;
import org.example.courzelo.serviceImpls.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImp implements IRatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Rating addRatingToTicket(String ticketId, Rating rating) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            rating.setTicket(ticket); // Set the ticket in the rating
            Rating savedRating = ratingRepository.save(rating); // Save the rating to the repository
            return savedRating; // Return the saved rating
        } else {
            throw new RuntimeException("Ticket not found with id: " + ticketId);
        }
    }


    @Override
    public Rating updateRating(String ratingId, Rating rating) {
        Optional<Rating> ratingOptional = ratingRepository.findById(ratingId);
        if (ratingOptional.isPresent()) {
            Rating existingRating = ratingOptional.get();
            existingRating.setRating(rating.getRating());
            Rating updatedRating = ratingRepository.save(existingRating);
            return updatedRating;
        } else {
            throw new RuntimeException("Rating not found with id: " + ratingId);
        }
    }

    @Override
    public Rating getRatingById(String ratingId) {
        return ratingRepository.findById(ratingId).orElseThrow(() ->
                new RuntimeException("Rating not found with id: " + ratingId));
    }

    @Override
    public void deleteRating(String ratingId) {
        Optional<Rating> ratingOptional = ratingRepository.findById(ratingId);
        if (ratingOptional.isPresent()) {
            Rating rating = ratingOptional.get();
            ratingRepository.deleteById(rating.getId());
            // Update the associated ticket's average rating
        } else {
            throw new RuntimeException("Rating not found with id: " + ratingId);
        }
    }

    @Override
    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }
}
