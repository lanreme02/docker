package com.cognizant.goldenretriever.portal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
final class VisitorPortal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vp_generator")
    @SequenceGenerator(name="vp_generator", sequenceName = "vp_sequence", allocationSize=1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    Employee employee;

    private final String badgeId;
    private final Date checkinTime;

    private Date checkoutTime;


    VisitorPortal(){
        this.id=0L;
        this.badgeId=null;
        this.checkinTime =null;
        this.checkoutTime =null;
    }

    @JsonCreator
    VisitorPortal(@JsonProperty("badgeId") final String badgeId, @JsonProperty("checkinTime") final Date checkinTime,  @JsonProperty("checkoutTime") final Date checkoutTime){
        this.badgeId=badgeId;
        this.checkinTime=checkinTime;
        this.checkoutTime=checkoutTime;
    }

    @JsonCreator
    VisitorPortal(@JsonProperty("employeeId") Employee employee ,@JsonProperty("badgeId") final String badgeId, @JsonProperty("checkinTime") final Date checkinTime,  @JsonProperty("checkoutTime") final Date checkoutTime){
        this.employee=employee;
        this.badgeId=badgeId;
        this.checkinTime=checkinTime;
        this.checkoutTime=checkoutTime;
    }

    public long getId() {
        return id;
    }
    public String getBadgeId() {
        return badgeId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public Date getCheckinTime() {
        return checkinTime;
    }

    public Date getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime) {
        this.checkoutTime = checkoutTime;
    }
}
