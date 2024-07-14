package com.devjoemar.intrinsicvalue.api;

import java.math.BigDecimal;

/**
 * Response model for the intrinsic value calculation result.
 */
public class IntrinsicValueResponse {

    private BigDecimal intrinsicValue;
    private String currency;
    private String remarks;

    public IntrinsicValueResponse(BigDecimal intrinsicValue, String currency, String remarks) {
        this.intrinsicValue = intrinsicValue;
        this.currency = currency;
        this.remarks = remarks;
    }

    // Getters and Setters
    public BigDecimal getIntrinsicValue() {
        return intrinsicValue;
    }

    public void setIntrinsicValue(BigDecimal intrinsicValue) {
        this.intrinsicValue = intrinsicValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}