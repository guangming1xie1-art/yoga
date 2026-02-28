package com.yoga.common.dto.session;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SessionVO {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String coverImage;
    private Integer durationMinutes;
    private Integer capacity;
    private Integer bookedCount;
    private Integer remainingSeats;
    private BigDecimal price;
    private String difficulty;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Long coachId;
    private String coachName;
    private String coachAvatar;
    private Long venueId;
    private String venueName;
    private String venueAddress;
}
