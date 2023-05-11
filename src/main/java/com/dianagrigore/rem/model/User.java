package com.dianagrigore.rem.model;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.dianagrigore.rem.model.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity(name = "app_user")
@Table(indexes = {@Index(columnList = "name"), @Index(columnList = "email")})
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String userId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 500)
    private String bio;

    @Column(length = 100, nullable = false)
    private String location;

    @Column(length = 100)
    private String gender;

    @Column
    private Date birthday;

    @Column(length = 100)
    private String url;

    @Column(columnDefinition = "integer DEFAULT 10")
    private int pagePreference;

    @Column(nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private UserType type;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonProperty(access = WRITE_ONLY)
    private UserLogin login;

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonProperty(access = WRITE_ONLY)
    private List<AgentListing> listings;

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonProperty(access = WRITE_ONLY)
    private List<AgentListing> listingsCreated;

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonProperty(access = WRITE_ONLY)
    private List<Offer> offers;

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonProperty(access = WRITE_ONLY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "creator")
    @JsonProperty(access = WRITE_ONLY)
    private List<Review> reviewsCreated;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public List<AgentListing> getListings() {
        return listings;
    }

    public void setListings(List<AgentListing> listings) {
        this.listings = listings;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserLogin getLogin() {
        return login;
    }

    public void setLogin(UserLogin login) {
        this.login = login;
    }

    public int getPagePreference() {
        return pagePreference;
    }

    public void setPagePreference(int pagePreference) {
        this.pagePreference = pagePreference;
    }

    public List<AgentListing> getListingsCreated() {
        return listingsCreated;
    }

    public void setListingsCreated(List<AgentListing> listingsCreated) {
        this.listingsCreated = listingsCreated;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviewsCreated() {
        return reviewsCreated;
    }

    public void setReviewsCreated(List<Review> reviewsCreated) {
        this.reviewsCreated = reviewsCreated;
    }
}
