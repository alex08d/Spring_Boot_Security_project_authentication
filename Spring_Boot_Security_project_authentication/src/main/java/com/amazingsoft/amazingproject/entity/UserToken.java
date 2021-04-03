package com.amazingsoft.amazingproject.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_token")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @ManyToOne(targetEntity = User.class, optional = false)
    private User user;

    @Column(name = "emitted_at_timestamp", nullable = false)
    private long emittedAtTimestamp;

    @Column(name = "expires_at_timestamp", nullable = false)
    private long expiresAtTimestamp;

    public long getId() {
        return id;
    }

    public UserToken setId(long id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserToken setToken(String token) {
        this.token = token;
        return this;
    }

    public UserToken setUser(User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return user;
    }

    public long getEmittedAtTimestamp() {
        return emittedAtTimestamp;
    }

    public UserToken setEmittedAtTimestamp(long emittedAtTimestamp) {
        this.emittedAtTimestamp = emittedAtTimestamp;
        return this;
    }

    public long getExpiresAtTimestamp() {
        return expiresAtTimestamp;
    }

    public UserToken setExpiresAtTimestamp(long expiresAtTimestamp) {
        this.expiresAtTimestamp = expiresAtTimestamp;
        return this;
    }
}
