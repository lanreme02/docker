package com.cognizant.goldenretriever.portal;

import org.springframework.stereotype.Service;

@Service
public interface BadgeService {

    String getBadgeWithEmpId(String empId) ;

    String returnBadge(String employeeId, String badgeId);
}
