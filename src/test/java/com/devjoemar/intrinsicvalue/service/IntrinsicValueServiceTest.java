package com.devjoemar.intrinsicvalue.service;

import com.devjoemar.intrinsicvalue.api.IntrinsicValueCalculationDto;
import com.devjoemar.intrinsicvalue.api.IntrinsicValueResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for IntrinsicValueService.
 * <p>
 * These tests validate the correctness of the intrinsic value calculation for various scenarios.
 * Each test case is designed to verify a specific aspect of the calculation to ensure robustness and accuracy.
 * </p>
 * <p>
 * <b>Reasoning Behind Each Test Case:</b>
 * <ul>
 *   <li><b>calculateIntrinsicValue_shouldReturnCorrectIntrinsicValueAndRemark_whenInputIsValid:</b>
 *       Verifies that the intrinsic value calculation returns the correct value and remark for valid input data.
 *       This test ensures that the calculation logic is correct under normal circumstances.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldReturnBelowZeroIntrinsicValue_whenFCFIsZero:</b>
 *       Checks if the intrinsic value calculation returns a value below zero when the Free Cash Flow (FCF) is zero.
 *       This test ensures that the logic correctly handles cases where the company generates no free cash flow.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldReturnHigherIntrinsicValue_whenNetDebtIsNegative:</b>
 *       Ensures that the intrinsic value calculation returns a higher value when the net debt is negative.
 *       Negative net debt indicates that the company has more cash than debt, increasing its intrinsic value.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldReturnLowerIntrinsicValue_whenGrowthRateIsZero:</b>
 *       Verifies that the intrinsic value calculation returns a lower value when the growth rate is zero.
 *       This test ensures that the calculation correctly reflects the lack of growth in future cash flows.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldReturnLowerIntrinsicValue_whenTerminalGrowthRateIsZero:</b>
 *       Checks if the intrinsic value calculation returns a lower value when the terminal growth rate is zero.
 *       This test ensures that the logic correctly handles cases where there is no growth expected beyond the forecast period.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldHandleLowGrowthRateAndHighDiscountRate:</b>
 *       Ensures that the intrinsic value calculation handles scenarios with low growth rate and high discount rate correctly.
 *       This test verifies that the calculation reflects higher perceived risk and lower growth expectations.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldHandleHighGrowthRateAndLowDiscountRate:</b>
 *       Verifies that the intrinsic value calculation handles scenarios with high growth rate and low discount rate correctly.
 *       This test ensures that the calculation reflects lower perceived risk and higher growth expectations.</li>
 * </ul>
 * </p>
 */
class IntrinsicValueServiceTest {

    private IntrinsicValueService intrinsicValueService;

    @BeforeEach
    void setUp() {
        intrinsicValueService = new IntrinsicValueService();
    }

    @Test
    void calculateIntrinsicValue_shouldReturnCorrectIntrinsicValueAndRemark_whenInputIsValid() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isEqualByComparingTo(new BigDecimal("318.91"));
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Undervalued");
    }

    @Test
    void calculateIntrinsicValue_shouldReturnBelowZeroIntrinsicValue_whenFCFIsZero() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(BigDecimal.ZERO)
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isLessThan(BigDecimal.ZERO);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }

    @Test
    void calculateIntrinsicValue_shouldReturnHigherIntrinsicValue_whenNetDebtIsNegative() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("-0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isGreaterThan(new BigDecimal("318.91"));
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Undervalued");
    }

    @Test
    void calculateIntrinsicValue_shouldReturnLowerIntrinsicValue_whenGrowthRateIsZero() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(BigDecimal.ZERO)
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isLessThan(new BigDecimal("318.91"));
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }

    @Test
    void calculateIntrinsicValue_shouldReturnLowerIntrinsicValue_whenTerminalGrowthRateIsZero() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(BigDecimal.ZERO)
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isLessThan(new BigDecimal("318.91"));
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }


    @Test
    void calculateIntrinsicValue_shouldHandleLowGrowthRateAndHighDiscountRate() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.01"))
                .discountRate(new BigDecimal("0.20"))
                .terminalGrowthRate(new BigDecimal("0.01"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isLessThan(new BigDecimal("318.91"));
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }

    @Test
    void calculateIntrinsicValue_shouldHandleHighGrowthRateAndLowDiscountRate() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.25"))
                .discountRate(new BigDecimal("0.05"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isGreaterThan(new BigDecimal("318.91"));
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Undervalued");
    }


}