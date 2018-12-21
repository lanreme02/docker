package com.cognizant.goldenretriever.portal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_generator")
    @SequenceGenerator(name="employee_generator", sequenceName = "employee_sequence", allocationSize=1)
    private long id;

    @Column(name="employeeId", unique=true)
    private final String employeeId;
    private final String phoneNumber;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    Set<VisitorPortal> visitors = new HashSet<VisitorPortal>();

    public void addVisitor(VisitorPortal visitorPortal){

        visitors.add(visitorPortal);
        visitorPortal.setEmployee(this);

    }

    public Employee(){
        this.id=0L;
        this.employeeId =null;
        this.phoneNumber=null;
    }

    @JsonCreator
    public Employee(@JsonProperty("employeeId") final String employeeId, @JsonProperty("phoneNumber") String phoneNumber){
        this.employeeId = employeeId;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return
                Objects.equals(employeeId, employee.employeeId) &&
                Objects.equals(phoneNumber, employee.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeId, phoneNumber);
    }
}
