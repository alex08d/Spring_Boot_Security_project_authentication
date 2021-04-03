package com.amazingsoft.amazingproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, optional = false)
    private User user;

    @JsonProperty
    @Column(name = "from_timestamp", nullable = false)
    private long fromTimestamp;

    @JsonProperty
    @Column(name = "to_timestamp", nullable = false)
    private long toTimestamp;

    public Booking setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Booking setUser(User user) {
        this.user = user;
        return this;
    }

    public long getFromTimestamp() {
        return fromTimestamp;
    }

    public Booking setFromTimestamp(long fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
        return this;
    }

    public long getToTimestamp() {
        return toTimestamp;
    }

    public Booking setToTimestamp(long toTimestamp) {
        this.toTimestamp = toTimestamp;
        return this;
    }
}
