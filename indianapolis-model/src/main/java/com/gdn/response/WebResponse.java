package com.gdn.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class WebResponse<Response> {
    private Integer code;

    private String status;

    private String message;

    private Response data;

    public static <Response> WebResponse<Response> OK(Response data){
        return WebResponse.<Response>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .build();
    }

    public static <Response> WebResponse<Response> NOT_FOUND(){
        return WebResponse.<Response>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .data(null)
                .build();
    }

    public static <Response> WebResponse<Response> ERROR(String error){
        return WebResponse.<Response>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(error)
                .data(null)
                .build();
    }
}
