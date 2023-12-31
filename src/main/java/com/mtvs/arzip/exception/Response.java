package com.mtvs.arzip.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T message;

    public static<T> Response<T> error(T result){
        return new Response("ERROR", result);
    }

    public static<T> Response<T> success(T result){
        return new Response("SUCCESS", result);
    }

}
