package com.devjoemar.intrinsicvalue.util;


import com.devjoemar.intrinsicvalue.api.IntrinsicValueCalculationDto;
import com.devjoemar.intrinsicvalue.api.IntrinsicValueRequest;
import org.springframework.stereotype.Component;

/**
 * Transformer class to convert IntrinsicValueRequest to IntrinsicValueCalculationDto.
 */
@Component
public class IntrinsicValueTransformer {

    /**
     * Transforms an IntrinsicValueRequest into an IntrinsicValueCalculationDto.
     *
     * @param request the intrinsic value request
     * @return the intrinsic value calculation DTO
     */
    public IntrinsicValueCalculationDto transformToDto(IntrinsicValueRequest request) {
        return IntrinsicValueCalculationDto.builder()
                .fcfLastYear(request.getFcfLastYear())
                .growthRate(request.getGrowthRate())
                .discountRate(request.getDiscountRate())
                .terminalGrowthRate(request.getTerminalGrowthRate())
                .sharesOutstanding(request.getSharesOutstanding())
                .netDebt(request.getNetDebt())
                .currentMarketPrice(request.getCurrentMarketPrice())
                .build();
    }
}
