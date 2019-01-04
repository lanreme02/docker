package com.cognizant.goldenretriever.portal;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final VisitorPortalRepository visitorPortalRepository;

    private final BadgeService badgeService;

    EmployeeService(EmployeeRepository employeeRepository, VisitorPortalRepository visitorPortalRepository, BadgeService badgeService) {
        this.employeeRepository = employeeRepository;
        this.visitorPortalRepository = visitorPortalRepository;
        this.badgeService = badgeService;
    }


    String checkin(Employee employee) {


        if (employeeRepository.findByEmployeeId(employee.getEmployeeId()).isPresent()) {
            Optional<Employee> existingEmployee = employeeRepository.findByEmployeeId(employee.getEmployeeId());
            VisitorPortal visitorEntry = new VisitorPortal(existingEmployee.get(), badgeService.getBadgeWithEmpId(employee.getEmployeeId()), new Date(), null);
            visitorPortalRepository.save(visitorEntry);
            return "";
        }

        VisitorPortal visitorEntry = new VisitorPortal(badgeService.getBadgeWithEmpId(employee.getEmployeeId()), new Date(), null);
        employee.addVisitor(visitorEntry);
        employeeRepository.save(employee);
        return visitorEntry.getBadgeId();
    }

    Employee checkout(Employee employee) {
        VisitorPortal visitorPortal = employeeRepository.findByEmployeeId(
                employee.getEmployeeId()).get()
                .visitors
                .stream()
                .findFirst().get();
        visitorPortal.setCheckoutTime(new Date());

        visitorPortalRepository.save(visitorPortal);
        badgeService.returnBadge(employee.getEmployeeId(), visitorPortal.getBadgeId());
        return employee;
    }
}