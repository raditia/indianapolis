package com.gdn.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SchedulingResponse {

    private Date startTime;

    private Date endTime;

    private long duration;

}
