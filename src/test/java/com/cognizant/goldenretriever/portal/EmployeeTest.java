package com.cognizant.goldenretriever.portal;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

public class EmployeeTest {

    @Test
    public void equalsAndHashCodeContractIsValid() {
        final String angelaMobile = "+11234567890";
        final String angelaEmployeeId = "101";

        new EqualsTester()
                .addEqualityGroup(
                        new Employee(angelaEmployeeId,  angelaMobile),
                        new Employee( angelaEmployeeId,  angelaMobile)
                )
                .addEqualityGroup(new Employee( "101_",  angelaMobile))
                .addEqualityGroup(new Employee( angelaEmployeeId,  "+11234567890_"))
                .addEqualityGroup(new Employee( null,  angelaMobile))
                .addEqualityGroup(new Employee( angelaEmployeeId,  null))
                .addEqualityGroup(
                        new Employee( null, null),
                        new Employee( null, null)
                )
                .testEquals();
    }

}
