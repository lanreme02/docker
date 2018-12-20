package com.cognizant.goldenretriever.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
final class EmployeeService {

    @Autowired
    EmployeeRepository repository;

    @Autowired
    VisitorPortalRepository visitorPortalRepository;

    BadgeService badgeService = new BadgeMockService();

    void setBadgeService(BadgeService badgeService){
       this.badgeService = badgeService;
    }

    public void checkin(Employee employee){


        if(repository.findByEmployeeId(employee.getEmployeeId()).isPresent()){
            Optional<Employee> existingEmployee = repository.findByEmployeeId(employee.getEmployeeId());
            VisitorPortal visitorEntry = new VisitorPortal(existingEmployee.get(),badgeService.getBadge(),new Date(),null);
            visitorPortalRepository.save(visitorEntry);
            return;
        }

        VisitorPortal visitorEntry = new VisitorPortal(badgeService.getBadge(),new Date(),null);
        employee.addVisitor(visitorEntry);
        repository.save(employee);
    }

    public Employee checkout(Employee employee) {
        VisitorPortal visitorPortal =   repository.findByEmployeeId(
                employee.getEmployeeId()).get()
                .visitors
                .stream()
                .filter(value->value.getCheckinTime().getDay()== Calendar.getInstance().getTime().getDay())
                .findFirst().get();
        visitorPortal.setCheckoutTime(new Date());
        //employee.visitors.clear();
        //employee.addVisitor(visitorPortal);
        visitorPortalRepository.save(visitorPortal);
        return employee;
    }
}
