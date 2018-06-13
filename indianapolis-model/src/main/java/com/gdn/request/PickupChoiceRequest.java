package com.gdn.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PickupChoiceRequest {

    public String recommendationResultId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date pickupDate;

}
