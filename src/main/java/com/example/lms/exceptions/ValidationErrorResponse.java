package com.example.lms.exceptions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;
    private List<ValidationErrorDetail> details;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ValidationErrorDetail {
        private String field;
        private String error;
    }
}
