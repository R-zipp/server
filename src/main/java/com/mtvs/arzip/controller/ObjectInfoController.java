package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.object_info.ObjectFileDataRequest;
import com.mtvs.arzip.domain.dto.object_info.ObjectInfoDto;
import com.mtvs.arzip.domain.dto.object_info.ObjectInfoResponse;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.ObjectInfoService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/object")
@Api(tags = "오브젝트 데이터 저장")
public class ObjectInfoController {

    private final ObjectInfoService objectInfoService;

    // 파일 저장 용도
    @PostMapping(value = "/save", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public Response<ObjectInfoResponse> objectFileSave(InputStream stream, @RequestPart ObjectFileDataRequest objectInfoRequest,
                                  @RequestHeader("Content-Type")String contentType) throws IOException {

        String etc;
        try {
            etc = getfileExtension(contentType);
        } catch (UnsupportedOperationException e) {
            throw new AppException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        ObjectInfoDto objectInfoDto;

        try {
            objectInfoDto = objectInfoService.objectFileSave(stream, objectInfoRequest, etc, contentType);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        return Response.success(new ObjectInfoResponse("파일 업로드 완료", objectInfoDto.getNo()));
    }

    // 오브젝트 타입, 이름 저장 용도
    @PostMapping("/stringSave/{objectInfoNo}")
    public Response<ObjectInfoResponse> objectStringSave(@PathVariable Long objectInfoNo, @RequestBody String json) {
        try {
            objectInfoService.saveStringData(json, objectInfoNo);
        } catch (Exception e) {
            throw new AppException(ErrorCode.JSON_DATA_PARSING_ERROR);
        }

        return Response.success(new ObjectInfoResponse("추가 내용 저장 완료", objectInfoNo));
    }



    private String getfileExtension(String contentType) {
        switch (contentType) {
            case MediaType.IMAGE_JPEG_VALUE:
                return ".jpeg";

            case MediaType.IMAGE_PNG_VALUE:
                return ".png";

            case MediaType.APPLICATION_OCTET_STREAM_VALUE:
                return ".fbx";

            default:
                throw new UnsupportedOperationException("UnSupported ContentType : " + contentType);
        }
    }

}
