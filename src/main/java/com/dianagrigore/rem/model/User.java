package com.dianagrigore.rem.model;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.dianagrigore.rem.converter.PasswordEncryptor;
import com.dianagrigore.rem.model.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity(name = "app_user")
@Table(indexes = {@Index(columnList = "name"), @Index(columnList = "email"), @Index(columnList = "email, password")})
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String userId;

    @Column(length = 100)
    private String name;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 100)
    @JsonProperty(access = WRITE_ONLY)
    @Convert(converter = PasswordEncryptor.class)
    private String password;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private UserType type;

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonProperty(access = WRITE_ONLY)
    private List<AgentListing> listings;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
