package com.cognizant.goldenretriever.portal;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class VisitorPortalTest {

    @Test
    public void returnVisitorPortalWithRightInformation(){
        Employee employee = new Employee();
        VisitorPortal visitorPortal = new VisitorPortal(employee ,"123", null, null);
        assertTrue(visitorPortal.getBadgeId()=="123");
        assertTrue(visitorPortal.getEmployee()==employee);
        assertTrue(visitorPortal.getCheckinTime()==null);
        assertTrue(visitorPortal.getCheckoutTime()==null);



    }
}
