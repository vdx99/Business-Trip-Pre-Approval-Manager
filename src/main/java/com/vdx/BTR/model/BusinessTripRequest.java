package com.vdx.BTR.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "business_trip_requests")
public class BusinessTripRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String tripReason;

    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String startLocation;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be >= 0")
    private BigDecimal anticipatedExpenseAmount;

    @Column(length = 2000)
    private String comments;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING / APPROVED / REJECTED

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; // właściciel wniosku

    public BusinessTripRequest() {
    }

    // GETTERY / SETTERY
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTripReason() {
        return tripReason;
    }

    public void setTripReason(String tripReason) {
        this.tripReason = tripReason;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getAnticipatedExpenseAmount() {
        return anticipatedExpenseAmount;
    }

    public void setAnticipatedExpenseAmount(BigDecimal anticipatedExpenseAmount) {
        this.anticipatedExpenseAmount = anticipatedExpenseAmount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
