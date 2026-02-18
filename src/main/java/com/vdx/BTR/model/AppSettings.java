package com.vdx.BTR.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "app_settings")
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal approvalThreshold;

    public AppSettings() {
    }

    public AppSettings(BigDecimal approvalThreshold) {
        this.approvalThreshold = approvalThreshold;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getApprovalThreshold() {
        return approvalThreshold;
    }

    public void setApprovalThreshold(BigDecimal approvalThreshold) {
        this.approvalThreshold = approvalThreshold;
    }
}
