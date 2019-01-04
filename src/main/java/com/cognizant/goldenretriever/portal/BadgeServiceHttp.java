package com.cognizant.goldenretriever.portal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
final class BadgeServiceHttp implements BadgeService {

    private final RestTemplate restTemplate;

    @Value("${badge.service.url}")
    private String url;

    public BadgeServiceHttp() {
        restTemplate = new RestTemplate();
    }

    @Override
    public String getBadgeWithEmpId(String empId) {
        BadgeRequest request = new BadgeRequest(empId, null);

        BadgeResult response =
                restTemplate.postForObject(url + "/requestBadge", request, BadgeResult.class);
        return response.getBadgeId();
    }

    @Override
    public String returnBadge(String employeeId, String badgeId) {

        BadgeRequest request = new BadgeRequest(employeeId, badgeId);

        BadgeResult response =
                restTemplate.postForObject(url + "/returnBadge", request, BadgeResult.class);
        return response.getBadgeId();

    }
}

