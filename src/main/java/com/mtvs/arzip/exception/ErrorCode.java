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
    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "User Email이 중복됩니다."),
    DUPLICATED_USER_NICKNAME(HttpStatus.CONFLICT, "User NickName이 중복됩니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 업로드 에러"),
    JSON_DATA_PARSING_ERROR(HttpStatus.BAD_REQUEST, "Json 데이터 파싱 중 오류가 발생했습니다."),
    AI_SERVICE_ERROR(HttpStatus.BAD_REQUEST,"AI 서비스 응답 오류"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 올린 도면 이미지를 찾을 수 없습니다."),
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND,"추가 데이터를 저장할 오브젝트 파일이 없습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 확장자입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "Refresh Token이 유효하지 않습니다."),
    LOGOUT_USER(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다."),
    TOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED,"토큰의 유저 정보가 일치하지 않습니다"),
    AI_DRAWING_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "ai로 생성된 공간 정보를 찾을 수 없습니다."),
    OBJECT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "오브젝트가 존재하지 않습니다."),
    SPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 공간 정보가 존재하지 않습니다."),
    SPACE_DATA_SAVING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"공간 정보 저장 중 오류가 발생했습니다."),
    SPACE_DATA_LOADING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "공간 정보 조회 중 오류가 발생했습니다."),


    ;






    ;
    private HttpStatus status;
    private String message;
}
