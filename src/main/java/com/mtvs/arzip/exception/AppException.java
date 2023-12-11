package com.mtvs.arzip.exception;

import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class AppException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public AppException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = getMessage();
    }

    @Override
    public String toString() {
        return message;
    }
}
