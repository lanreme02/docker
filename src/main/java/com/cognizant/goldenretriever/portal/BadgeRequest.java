package com.cognizant.goldenretriever.portal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BadgeRequest {

    private String employeeId;

    private String badgeNumber;

    @JsonCreator
    public BadgeRequest(@JsonProperty("employeeId") final String employeeId,
                        @JsonProperty("badgeNumber") final String badgeNumber){
        this.employeeId = employeeId;
        this.badgeNumber = badgeNumber;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }
    public String getEmployeeId() {
        return employeeId;
    }

}
