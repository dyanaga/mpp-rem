package com.dianagrigore.rem.service;

import com.dianagrigore.rem.model.AgentListing;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.model.Review;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.repository.AgentListingRepository;
import com.dianagrigore.rem.repository.ListingRepository;
import com.dianagrigore.rem.repository.OfferRepository;
import com.dianagrigore.rem.repository.ReviewRepository;
import com.dianagrigore.rem.repository.UserRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
public class DataGenerationService {

    private final static List<String> neighbourhood = List.of("Andrei Mureșanu", "Bulgaria", "Buna ziua", "Centru", "Europa", "Manastur", "Zorilor", "Iris", "Marasti",
            "Gheorgheni");
    private final static Random random = new Random();

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;
    private final AgentListingRepository agentListingRepository;
    private final ListingRepository listingRepository;
    private final Faker faker = new Faker();

    public DataGenerationService(UserRepository userRepository, ReviewRepository reviewRepository, OfferRepository offerRepository, AgentListingRepository agentListingRepository
            , ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.offerRepository = offerRepository;
        this.agentListingRepository = agentListingRepository;
        this.listingRepository = listingRepository;
    }

    public void cleanup() {
        reviewRepository.deleteAllInBatch();
        offerRepository.deleteAllInBatch();
        agentListingRepository.deleteAllInBatch();
        listingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    public void batch() {
        cleanup();
        generate(0);

    }

    public void millions() {
//        cleanup();
        for (int i = 1; i < 1000; i++) {
            generate((i - 1) * 1000);
            log.info("{}.{}%", i / 100, i % 100);
        }
    }

    @Transactional
    public void generate(int base) {

        int count = 1000;
        int phoneBase = 700000000;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setName(faker.name().fullName());
            user.setEmail(faker.name().username() + (base + i) + "@gmail.com");
            user.setPassword("Strong123#");
            user.setPhoneNumber("0" + (phoneBase + base + i));
            user.setType(UserType.DIRECTOR);
            users.add(user);
        }
        List<User> savedUsers = userRepository.saveAll(users);
        List<Listing> listings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Listing listing = new Listing();
            listing.setSuggestedPrice(faker.number().numberBetween(10000, 200000));
            listing.setRooms(faker.number().numberBetween(1, 5));
            listing.setDescription(faker.backToTheFuture().quote());
            listing.setName(faker.funnyName().name());
            listing.setSize(faker.number().numberBetween(20, 120));
            listing.setNeighbourhood(neighbourhood.get(faker.number().numberBetween(0, 9)));
            listing.setAddress(faker.address().fullAddress());
            listings.add(listing);
        }

        List<Listing> savedListings = listingRepository.saveAll(listings);
        List<AgentListing> agentListings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Set<Integer> numbers = new HashSet<>();
            while (numbers.size() < 10) {
                int number = random.nextInt(1000);
                numbers.add(number);
            }
            List<Integer> randomUsers = new ArrayList<>(numbers);
            for (int j = 0; j < 10; j++) {
                AgentListing agentListing = new AgentListing();

                agentListing.setListingId(savedListings.get(i).getListingId());
                agentListing.setListing(savedListings.get(i));
                agentListing.setUserId(savedUsers.get(randomUsers.get(j)).getUserId());
                agentListing.setUser(savedUsers.get(randomUsers.get(j)));
                agentListings.add(agentListing);

            }
        }
        agentListingRepository.saveAll(agentListings);

        List<Offer> offers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Offer offer = new Offer();
            int listing = faker.number().numberBetween(0, 999);
            int agent = faker.number().numberBetween(0, 999);
            offer.setListingId(savedListings.get(listing).getListingId());
            offer.setUserId(savedUsers.get(agent).getUserId());
            offer.setComment(faker.chuckNorris().fact());
            offer.setPrice(faker.number().numberBetween(1000, 300000));
            offers.add(offer);
        }
        offerRepository.saveAll(offers);
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Review review = new Review();
            int agent = faker.number().numberBetween(0, 999);
            review.setUser(savedUsers.get(agent));
            review.setUserId(savedUsers.get(agent).getUserId());
            review.setReview(faker.elderScrolls().quote());
            review.setStars(faker.number().numberBetween(1, 5));
            reviews.add(review);
        }
        reviewRepository.saveAll(reviews);

    }

}
