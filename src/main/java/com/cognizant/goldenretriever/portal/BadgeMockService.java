package com.cognizant.goldenretriever.portal;

import java.util.Random;

final class BadgeMockService implements BadgeService {
    @Override
    public String getBadge() {
        Random rnd = new Random();
        int badgeId = 100000 + rnd.nextInt(900000);
        return ""+badgeId;
    }
}
