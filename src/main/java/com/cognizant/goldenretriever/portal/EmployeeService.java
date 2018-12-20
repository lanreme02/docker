package com.cognizant.goldenretriever.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
final class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    VisitorPortalRepository visitorPortalRepository;

    BadgeService badgeService = new BadgeServiceHttp();

    void setBadgeService(BadgeService badgeService){
       this.badgeService = badgeService;
    }

    public String checkin(Employee employee) throws Exception{


        if(employeeRepository.findByEmployeeId(employee.getEmployeeId()).isPresent()){
            Optional<Employee> existingEmployee = employeeRepository.findByEmployeeId(employee.getEmployeeId());
            VisitorPortal visitorEntry = new VisitorPortal(existingEmployee.get(),badgeService.getBadgeWithEmpId(employee.getEmployeeId()),new Date(),null);
            visitorPortalRepository.save(visitorEntry);
            return "";
        }

        VisitorPortal visitorEntry = new VisitorPortal(badgeService.getBadgeWithEmpId(employee.getEmployeeId()),new Date(),null);
        employee.addVisitor(visitorEntry);
        employeeRepository.save(employee);
        return visitorEntry.getBadgeId();
    }

    public Employee checkout(Employee employee) throws Exception {
        VisitorPortal visitorPortal =   employeeRepository.findByEmployeeId(
                employee.getEmployeeId()).get()
                .visitors
                .stream()
                .filter(value->value.getCheckinTime().getDay()== Calendar.getInstance().getTime().getDay())
                .findFirst().get();
        visitorPortal.setCheckoutTime(new Date());

        visitorPortalRepository.save(visitorPortal);
        badgeService.returnBadge(visitorPortal.getBadgeId());
        return employee;
    }
}
