package com.devjoemar.intrinsicvalue.controller;


import com.devjoemar.intrinsicvalue.api.IntrinsicValueCalculationDto;
import com.devjoemar.intrinsicvalue.api.IntrinsicValueRequest;
import com.devjoemar.intrinsicvalue.api.IntrinsicValueResponse;
import com.devjoemar.intrinsicvalue.service.IntrinsicValueService;
import com.devjoemar.intrinsicvalue.util.IntrinsicValueTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for IntrinsicValueController.
 */
@WebMvcTest(IntrinsicValueController.class)
class IntrinsicValueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IntrinsicValueService intrinsicValueService;

    @MockBean
    private IntrinsicValueTransformer intrinsicValueTransformer;

    @BeforeEach
    void setUp() {
        IntrinsicValueCalculationDto dto = IntrinsicValueCalculationDto.builder()
                .fcfLastYear(new BigDecimal("1.1"))
                .growthRate(new BigDecimal("0.15"))
                .discountRate(new BigDecimal("0.10"))
                .terminalGrowthRate(new BigDecimal("0.03"))
                .sharesOutstanding(new BigDecimal("122"))
                .netDebt(new BigDecimal("0.5"))
                .currentMarketPrice(new BigDecimal("291.06"))
                .build();
        
        given(intrinsicValueTransformer.transformToDto(any(IntrinsicValueRequest.class))).willReturn(dto);
        
        given(intrinsicValueService.calculateIntrinsicValue(dto)).willReturn(new IntrinsicValueResponse(
                new BigDecimal("313.14"), "USD", "Undervalued"
        ));
    }

    @Test
    void calculateIntrinsicValue_shouldReturnCorrectResponse_whenInputIsValid() throws Exception {
        // Arrange
        String requestJson = """
                {
                  "fcfLastYear": 1.1,
                  "growthRate": 0.15,
                  "discountRate": 0.10,
                  "terminalGrowthRate": 0.03,
                  "sharesOutstanding": 122,
                  "netDebt": 0.5,
                  "currentMarketPrice": 291.06
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/intrinsic-value/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("list found"))
                .andExpect(jsonPath("$.internalCode").value("PRODUCT-10"))
                .andExpect(jsonPath("$.data.intrinsicValue").value(313.14))
                .andExpect(jsonPath("$.data.currency").value("USD"))
                .andExpect(jsonPath("$.data.remarks").value("Undervalued"));
    }
}
