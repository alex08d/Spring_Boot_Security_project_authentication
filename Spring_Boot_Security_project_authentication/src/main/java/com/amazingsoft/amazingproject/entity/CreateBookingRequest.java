package com.amazingsoft.amazingproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateBookingRequest {

    @JsonProperty
    private long fromTimestamp;

    @JsonProperty
    private long toTimestamp;

    public long getFromTimestamp() {
        return fromTimestamp;
    }

    public CreateBookingRequest setFromTimestamp(long fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
        return this;
    }

    public long getToTimestamp() {
        return toTimestamp;
    }

    public CreateBookingRequest setToTimestamp(long toTimestamp) {
        this.toTimestamp = toTimestamp;
        return this;
    }
}
