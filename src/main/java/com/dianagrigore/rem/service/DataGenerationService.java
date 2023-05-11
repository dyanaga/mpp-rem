package com.dianagrigore.rem.service;

import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.exception.BaseException;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.model.Review;
import com.dianagrigore.rem.model.enums.UserType;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class DataGenerationService {

    private final static List<String> neighbourhood = List.of("Andrei Mureșanu", "Bulgaria", "Buna ziua", "Centru", "Europa", "Manastur", "Zorilor", "Iris", "Marasti",
            "Gheorgheni");
    private final static Random random = new Random();

    private final DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private final Faker faker = new Faker();
    private final ApplicationContext applicationContext;

    public DataGenerationService(DataSource dataSource, ApplicationContext applicationContext) {
        this.dataSource = dataSource;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanup() {
        disableAll();
        try {
            jdbcTemplate.execute("TRUNCATE TABLE review");
            jdbcTemplate.execute("TRUNCATE TABLE offer");
            jdbcTemplate.execute("TRUNCATE TABLE agent_listing");
            jdbcTemplate.execute("TRUNCATE TABLE listing CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE app_user CASCADE");
        } finally {
            enableAll();
        }
    }

    @Transactional
    public void batch() {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");
        try {
            multiThreadInserts(executor, 0);
        } catch (InterruptedException e) {
            throw new BaseException("Failed to add batch.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Async("asyncExecutor")
    public void millions() {
//        disableAll();
        try {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");
            for (int i = 0; i < 100; i++) {
                multiThreadInserts(executor, i);
                log.info("{}%", (i + 1));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
//            enableAll();
        }
    }

    private void multiThreadInserts(ThreadPoolTaskExecutor executor, int i) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        for (int j = 0; j < 10; j++) {
            int index = i * 10 + j;
            executor.execute(() -> {
                try {
                    generate(index * 1000);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    private void generate(int base) {
        int count = 1000;
        int phoneBase = 700000000;
        String sql = "INSERT INTO app_user (user_id, name, email, phone_number, bio, location, gender, birthday, url, page_preference, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?)";
        List<Object[]> users = new ArrayList<>();
        List<Object[]> logins = new ArrayList<>();
        UserDto user = new UserDto();
        for (int i = 0; i < count; i++) {
            user.setName(faker.name().fullName());
            String username = faker.name().username() + (base + i);
            user.setEmail(username + "@gmail.com");
            user.setPassword("nJ8IjYf4+Iz4SFkkYqaRhw==");
            user.setUsername(username);
            user.setPhoneNumber("0" + (phoneBase + base + i));
            user.setType(UserType.DIRECTOR);
            user.setBio(faker.princessBride().quote());
            user.setGender(faker.funnyName().name());
            user.setBirthday(faker.date().birthday());
            user.setLocation(faker.address().city());
            user.setUrl("https://" + faker.internet().domainName());
            user.setPagePreference(faker.number().numberBetween(10, 55));
            String id = UUID.randomUUID().toString();
            users.add(new Object[]{id, user.getName(), user.getEmail(), user.getPhoneNumber(), user.getBio(), user.getLocation(), user.getGender(),
                    user.getBirthday(), user.getUrl(), user.getPagePreference(), user.getType().toString()});
            logins.add(new Object[]{id, user.getUsername(), user.getPassword(), new Date(), true});

        }
        jdbcTemplate.batchUpdate(sql, users);

        sql = "INSERT INTO user_login (user_login_id, username, password, timestamp, is_active) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, logins);


        sql = "INSERT INTO listing (listing_id, name, address, rooms, description, size, neighbourhood, suggested_price, creator) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> listings = new ArrayList<>();
        Listing listing = new Listing();
        for (int i = 0; i < count; i++) {
            listing.setSuggestedPrice(faker.number().numberBetween(10000, 200000));
            listing.setRooms(faker.number().numberBetween(1, 5));
            listing.setDescription(faker.backToTheFuture().quote());
            listing.setName(faker.funnyName().name());
            listing.setSize(faker.number().numberBetween(20, 120));
            listing.setNeighbourhood(neighbourhood.get(faker.number().numberBetween(0, 9)));
            listing.setAddress(faker.address().fullAddress());
            listing.setCreator(users.get(faker.number().numberBetween(0, 1000))[0].toString());
            listings.add(new Object[]{UUID.randomUUID().toString(), listing.getName(), listing.getAddress(), listing.getRooms(), listing.getDescription(), listing.getSize(),
                    listing.getNeighbourhood(), listing.getSuggestedPrice(), listing.getCreator()});
        }
        jdbcTemplate.batchUpdate(sql, listings);

        List<Object[]> agentListings = new ArrayList<>();
        sql = "INSERT INTO agent_listing (listing_id, user_id) VALUES (?, ?)";

        for (int i = 0; i < count; i++) {
            Set<Integer> numbers = new HashSet<>();
            while (numbers.size() < 10) {
                int number = random.nextInt(1000);
                numbers.add(number);
            }
            List<Integer> randomUsers = new ArrayList<>(numbers);
            for (int j = 0; j < 10; j++) {
                agentListings.add(new Object[]{listings.get(i)[0], users.get(randomUsers.get(j))[0]});

            }
        }
        jdbcTemplate.batchUpdate(sql, agentListings);

        sql = "INSERT INTO offer (offer_id, price, comment, timestamp, listing_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        List<Object[]> offers = new ArrayList<>();
        Offer offer = new Offer();
        for (int i = 0; i < count; i++) {
            int listing_index = faker.number().numberBetween(0, 999);
            int agent = faker.number().numberBetween(0, 999);
            offer.setComment(faker.chuckNorris().fact());
            offer.setPrice(faker.number().numberBetween(1000, 300000));
            offer.setTimestamp(new Date());
            offers.add(new Object[]{UUID.randomUUID().toString(), offer.getPrice(), offer.getComment(), offer.getTimestamp(), listings.get(listing_index)[0], users.get(agent)[0]});
        }
        jdbcTemplate.batchUpdate(sql, offers);

        sql = "INSERT INTO review (review_id, stars, review, timestamp, user_id, creator) VALUES (?, ?, ?, ?, ?, ?)";

        List<Object[]> reviews = new ArrayList<>();
        Review review = new Review();
        for (int i = 0; i < count; i++) {
            int agent = faker.number().numberBetween(0, 999);
            review.setReview(faker.elderScrolls().quote());
            review.setStars(faker.number().numberBetween(1, 5));
            review.setTimestamp(new Date());
            review.setCreator(users.get(faker.number().numberBetween(0, 1000))[0].toString());
            reviews.add(new Object[]{UUID.randomUUID().toString(), review.getStars(), review.getReview(), review.getTimestamp(), users.get(agent)[0], review.getCreator()});
        }
        jdbcTemplate.batchUpdate(sql, reviews);
    }

    private void disableAll() {
        jdbcTemplate.execute("ALTER TABLE app_user DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE listing DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE agent_listing DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE offer DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE review DISABLE TRIGGER ALL");
    }

    private void enableAll() {
        jdbcTemplate.execute("ALTER TABLE app_user ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE listing ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE agent_listing ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE offer ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE review ENABLE TRIGGER ALL");
    }

}
