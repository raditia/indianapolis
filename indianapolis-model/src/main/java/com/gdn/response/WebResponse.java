package com.gdn.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class WebResponse<Response> {
    private Integer code;

    private String status;

    private Response data;

    public static <Response> WebResponse<Response> OK(Response data){
        return WebResponse.<Response>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .build();
    }

    public static <Response> WebResponse<Response> NOT_FOUND(){
        return WebResponse.<Response>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();
    }
}
