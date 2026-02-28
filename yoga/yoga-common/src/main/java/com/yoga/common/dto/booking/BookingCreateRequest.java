package com.yoga.common.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingCreateRequest {
    @NotNull(message = "课程排期ID不能为空")
    private Long sessionId;
}
