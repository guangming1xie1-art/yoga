package com.yoga.common.dto.venue;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VenueDTO {
    @NotBlank(message = "场馆名称不能为空")
    private String name;
    @NotBlank(message = "城市不能为空")
    private String city;
    private String address;
    private String phone;
    private String businessHours;
    private String coverImage;
    private String description;
    private Integer status;
}
