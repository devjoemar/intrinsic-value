package com.devjoemar.intrinsicvalue.api;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for intrinsic value calculation.
 */
@Data
@Builder
public class IntrinsicValueCalculationDto {

    private BigDecimal fcfLastYear;
    private BigDecimal growthRate;
    private BigDecimal discountRate;
    private BigDecimal terminalGrowthRate;
    private BigDecimal sharesOutstanding;
    private BigDecimal netDebt;
    private BigDecimal currentMarketPrice;
}