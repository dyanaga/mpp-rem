package com.dianagrigore.rem.model;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity(name = "listing")
@Table(indexes = {@Index(columnList = "name"), @Index(columnList = "rooms")})
public class Listing {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String listingId;

    @Column(length = 100)
    private String name;

    @Column(length = 200)
    private String address;

    @Column
    private int rooms = 1;

    @Column(length = 500)
    private String description;

    @Column
    private int size = 5;

    @Column(length = 40)
    private String neighbourhood;

    @Column
    private int suggestedPrice = 0;

    @OneToMany
    @JoinColumn(name = "listing_id")
    @JsonProperty(access = WRITE_ONLY)
    private List<AgentListing> users;

    @OneToMany(mappedBy = "listing")
    @JsonProperty(access = WRITE_ONLY)
    private List<Offer> offers;

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public int getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(int suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public List<AgentListing> getUsers() {
        return users;
    }

    public void setUsers(List<AgentListing> users) {
        this.users = users;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
