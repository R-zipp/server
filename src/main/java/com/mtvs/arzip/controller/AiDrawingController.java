package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataAIRequest;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataFloorPlanRequest;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataResponse;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.AiDrawingService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/drawing")
@Api(tags = "도면 전송 및 저장")
public class AiDrawingController {

    private final AiDrawingService aiDrawingService;

    // 사용자가 올린 손도면 정보 저장, 데이터 전송
    @PostMapping(value = "/process", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
//    @PostMapping(value = "/process", consumes = {MediaType.IMAGE_PNG_VALUE})
    public String processAndStoreImage(InputStream stream, AiDrawingDataFloorPlanRequest request,
                                            @RequestHeader("Content-Type") String contentType) {

        System.out.println("contentType = " + contentType);
        System.out.println("stream = " + stream);

        String etc;
        try {
            etc = getfileExtension(contentType);
        } catch (UnsupportedOperationException e) {
            return Response.error(e.getMessage()).getMessage();
        }

        try {
            String result = aiDrawingService.userUploadFloorPlan(stream, request, etc, contentType);
            System.out.println("🏠Unreal이 받는 문자열: " + Response.success(result).getMessage());
            return Response.success(result).getMessage();
        } catch (IOException e) {
            return Response.error("이미지 처리 중 오류가 발생했습니다: " + e.getMessage()).getMessage();
        }
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