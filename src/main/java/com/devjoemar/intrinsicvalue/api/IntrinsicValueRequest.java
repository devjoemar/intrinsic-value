package com.devjoemar.intrinsicvalue.api;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request model for calculating the intrinsic value of a stock.
 */
@Data
@Builder
public class IntrinsicValueRequest {

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal fcfLastYear;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal growthRate;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal discountRate;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal terminalGrowthRate;

    @NotNull
    @Min(1)
    private BigDecimal sharesOutstanding;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal netDebt;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal currentMarketPrice;
}