package com.mtvs.arzip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 잘못되었습니다."),
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "UserId가 중복됩니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 업로드 에러"),
    JSON_DATA_PARSING_ERROR(HttpStatus.BAD_REQUEST, "Json 데이터 파싱 중 오류가 발생했습니다."),




    ;
    private HttpStatus status;
    private String message;
}
