package com.dianagrigore.rem.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "agent_listing")
@Entity
@IdClass(AgentListingKey.class)
//TODO remove `implements Persistable<AgentListingKey>`
public class AgentListing implements Persistable<AgentListingKey> {

    @Id
    @Column(name = "listing_id")
    private String listingId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "listing_id", insertable = false, updatable = false)
    Listing listing;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;



    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //TODO: remove next 2 lines

    @Override
    public AgentListingKey getId() {
        return new AgentListingKey(getUserId(), getListingId());
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
