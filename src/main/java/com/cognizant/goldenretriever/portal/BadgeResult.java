package com.cognizant.goldenretriever.portal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class BadgeResult {

    private final String badgeId;
    private final String statusMessage;

    @JsonCreator
    public BadgeResult(@JsonProperty("statusMessage") final String statusMessage, @JsonProperty("badgeId") final String badgeId) {
        this.statusMessage = statusMessage;
        this.badgeId = badgeId;
    }

    public String getBadgeId() {
        return badgeId;
    }

     }
