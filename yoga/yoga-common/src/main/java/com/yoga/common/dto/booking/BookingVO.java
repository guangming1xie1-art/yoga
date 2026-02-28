package com.yoga.common.dto.booking;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingVO {
    private Long id;
    private Long sessionId;
    private String sessionName;
    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;
    private String coachName;
    private String venueName;
    private BigDecimal price;
    private String status;
    private String qrCode;
    private LocalDateTime qrExpireAt;
    private LocalDateTime createdAt;
}
