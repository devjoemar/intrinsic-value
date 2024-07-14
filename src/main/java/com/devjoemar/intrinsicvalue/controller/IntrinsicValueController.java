package com.devjoemar.intrinsicvalue.controller;


import com.devjoemar.intrinsicvalue.api.ApiResponse;
import com.devjoemar.intrinsicvalue.api.IntrinsicValueRequest;
import com.devjoemar.intrinsicvalue.api.IntrinsicValueResponse;
import com.devjoemar.intrinsicvalue.service.IntrinsicValueService;
import com.devjoemar.intrinsicvalue.util.Constant;
import com.devjoemar.intrinsicvalue.util.IntrinsicValueTransformer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

/**
 * Controller for the intrinsic value calculator.
 */
@RestController
@RequestMapping("/api/v1/intrinsic-value")
@Validated
public class IntrinsicValueController {

    @Autowired
    private IntrinsicValueService intrinsicValueService;

    @Autowired
    private IntrinsicValueTransformer intrinsicValueTransformer;

    private final Map<String, String> responseMessages = Constant.getResponseHashMap();

    /**
     * Calculates the intrinsic value of a stock based on the provided inputs.
     *
     * @param request the intrinsic value request
     * @return the intrinsic value response
     */
    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<IntrinsicValueResponse>> doCalculateIntrinsicValue(@Valid @RequestBody IntrinsicValueRequest request) {
        var dto = intrinsicValueTransformer.transformToDto(request);
        IntrinsicValueResponse response = intrinsicValueService.calculateIntrinsicValue(dto);
        return ResponseEntity.ok(ApiResponse.ok(response,
                responseMessages,
                Constant.RESPONSE_CODE_PREFIX + "10"));
    }
}
