package com.devjoemar.intrinsicvalue.service;

import com.devjoemar.intrinsicvalue.api.IntrinsicValueCalculationDto;
import com.devjoemar.intrinsicvalue.api.IntrinsicValueResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for IntrinsicValueService.
 * <p>
 * <b>Purpose:</b> Validate the correctness and robustness of the intrinsic value calculation under various scenarios.
 * Each test case is designed to verify specific aspects of the calculation logic, ensuring it behaves as expected.
 * </p>
 * <p>
 * <b>Rationale Behind Each Test Case:</b>
 * <ul>
 *   <li><b>calculateIntrinsicValue_shouldReturnCorrectValue_whenInputIsValid:</b>
 *       Ensures that the service returns the expected intrinsic value and remark for valid input data.
 *       This is a standard scenario representing typical usage.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldReturnLessThanOrZeroIntrinsicValue_whenFCFIsZero:</b>
 *       Verifies that when the Free Cash Flow (FCF) is zero, the intrinsic value per share is zero or less.
 *       This checks how the service handles companies generating no free cash flow.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldReturnHigherValue_whenNetDebtIsNegative:</b>
 *       Checks that a negative net debt (more cash than debt) increases the intrinsic value.
 *       This reflects the company's stronger financial position.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldReturnLowerValue_whenGrowthRateIsZero:</b>
 *       Validates that a zero growth rate results in a lower intrinsic value, as future cash flows are not increasing.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldHandleHighDiscountRate:</b>
 *       Ensures that a high discount rate (reflecting higher risk) decreases the intrinsic value.
 *       This tests the sensitivity of the model to perceived investment risk.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldHandleLowDiscountRate:</b>
 *       Confirms that a low discount rate (reflecting lower risk) increases the intrinsic value.
 *       This tests the model's response to favorable investment conditions.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldThrowException_whenDiscountRateEqualsTerminalGrowthRate:</b>
 *       Verifies that the service correctly handles the mathematical error when the discount rate equals the terminal growth rate.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldThrowException_whenSharesOutstandingIsZero:</b>
 *       Ensures that the service throws an exception when shares outstanding is zero, as division by zero is undefined.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldHandleNegativeGrowthRate:</b>
 *       Tests how the service handles negative growth rates, representing declining companies.</li>
 *
 *   <li><b>calculateIntrinsicValue_shouldThrowException_whenInputIsNull:</b>
 *       Checks that the service validates null input and throws the appropriate exception.</li>
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
    void calculateIntrinsicValue_shouldReturnCorrectValue_whenInputIsValid() {
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

        // Expected intrinsic value calculated separately for accuracy
        BigDecimal expectedIntrinsicValue = new BigDecimal("318.91");

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isCloseTo(expectedIntrinsicValue, within(new BigDecimal("0.01")));
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Undervalued");
    }

    @Test
    void calculateIntrinsicValue_shouldReturnZeroIntrinsicValue_whenFCFIsZero() {
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
        assertThat(response.getIntrinsicValue()).isLessThanOrEqualTo(BigDecimal.ZERO);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }

    @Test
    void calculateIntrinsicValue_shouldReturnHigherValue_whenNetDebtIsNegative() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("-0.5")) // Negative net debt
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Expected intrinsic value should be higher than the base case
        BigDecimal baseIntrinsicValue = new BigDecimal("318.91");

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isGreaterThan(baseIntrinsicValue);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Undervalued");
    }

    @Test
    void calculateIntrinsicValue_shouldReturnLowerValue_whenGrowthRateIsZero() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(BigDecimal.ZERO) // Zero growth rate
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Expected intrinsic value should be lower than the base case
        BigDecimal baseIntrinsicValue = new BigDecimal("318.91");

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isLessThan(baseIntrinsicValue);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }

    @Test
    void calculateIntrinsicValue_shouldHandleHighDiscountRate() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.25")) // High discount rate
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Expected intrinsic value should be lower due to higher discount rate
        BigDecimal baseIntrinsicValue = new BigDecimal("318.91");

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isLessThan(baseIntrinsicValue);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }

    @Test
    void calculateIntrinsicValue_shouldHandleLowDiscountRate() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.05")) // Low discount rate
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Expected intrinsic value should be higher due to lower discount rate
        BigDecimal baseIntrinsicValue = new BigDecimal("318.91");

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isGreaterThan(baseIntrinsicValue);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getRemarks()).isEqualTo("Undervalued");
    }

    @Test
    void calculateIntrinsicValue_shouldThrowException_whenDiscountRateEqualsTerminalGrowthRate() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.03")) // Equal to terminal growth rate
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act & Assert
        assertThatThrownBy(() -> intrinsicValueService.calculateIntrinsicValue(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount rate and terminal growth rate cannot be equal.");
    }

    @Test
    void calculateIntrinsicValue_shouldThrowException_whenSharesOutstandingIsZero() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(BigDecimal.ZERO) // Zero shares outstanding
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();

        // Act & Assert
        assertThatThrownBy(() -> intrinsicValueService.calculateIntrinsicValue(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Shares outstanding must be greater than zero.");
    }

    @Test
    void calculateIntrinsicValue_shouldHandleNegativeGrowthRate() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("-0.05")) // Negative growth rate
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.02"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("200.00"))
                .build();

        // Act
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getIntrinsicValue()).isLessThan(new BigDecimal("200.00"));
        assertThat(response.getRemarks()).isEqualTo("Overvalued");
    }

    @Test
    void calculateIntrinsicValue_shouldThrowException_whenInputIsNull() {
        // Arrange
        IntrinsicValueCalculationDto dto = null;

        // Act & Assert
        assertThatThrownBy(() -> intrinsicValueService.calculateIntrinsicValue(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Input DTO cannot be null.");
    }

    @Test
    void calculateIntrinsicValue_shouldThrowException_whenInputParametersAreNull() {
        // Arrange
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder().build();

        // Act & Assert
        assertThatThrownBy(() -> intrinsicValueService.calculateIntrinsicValue(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more input parameters are null.");
    }
}
