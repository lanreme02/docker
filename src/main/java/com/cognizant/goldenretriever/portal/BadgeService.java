package com.cognizant.goldenretriever.portal;

import org.springframework.stereotype.Service;

@Service
public interface BadgeService {
    String getBadge();

    String getBadgeWithEmpId(String empId) throws Exception;

    String returnBadge(String badgeId) throws Exception;
}
