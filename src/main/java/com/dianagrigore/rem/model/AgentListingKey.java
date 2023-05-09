package com.dianagrigore.rem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AgentListingKey implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "listing_id")
    private String listingId;
}
