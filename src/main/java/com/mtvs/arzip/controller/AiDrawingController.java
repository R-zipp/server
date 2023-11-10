package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataFloorPlanRequest;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataHandingRequest;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.AiDrawingService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/drawing")
@Api(tags = "도면 전송 및 저장")
public class AiDrawingController {

    private final AiDrawingService aiDrawingService;

    // 사용자가 올린 일반 도면 정보 저장, 데이터 전송
    @PostMapping(value = "/process", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    // 'process?houseSize=' + houseSize
    public String uploadFloorPlan(InputStream stream, AiDrawingDataFloorPlanRequest request,
                                  @RequestHeader("Content-Type") String contentType
                              //    ,  @AuthenticationPrincipal Principal principal
    ) {

        System.out.println("넘어온 평수: " + request.getHouseSize());
        System.out.println("contentType = " + contentType);
        System.out.println("stream = " + stream);

        String etc;
        try {
            etc = getfileExtension(contentType);
        } catch (UnsupportedOperationException e) {
            return Response.error(e.getMessage()).getMessage();
        }

        try {
            // 호출 시 principal 파라미터 추가
            String result = aiDrawingService.userUploadFloorPlan(stream, request, etc, contentType, request.getHouseSize());  // , principal
            System.out.println("🏠Unreal이 받는 문자열: " + Response.success(result).getMessage());
            return Response.success(result).getMessage();
        } catch (IOException e) {
            return Response.error("이미지 처리 중 오류가 발생했습니다: " + e.getMessage()).getMessage();
        }

    }


    // 사용자가 올린 손도면 정보 저장, 데이터 전송
    @PostMapping(value = "/process/handimg", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public String uploadHandImg(InputStream stream, AiDrawingDataHandingRequest request,
                                       @RequestHeader("Content-Type") String contentType) {

        System.out.println("넘어온 평수: " + request.getHouseSize());
        System.out.println("contentType = " + contentType);
        System.out.println("stream = " + stream);

        String etc;
        try {
            etc = getfileExtension(contentType);
        } catch (UnsupportedOperationException e) {
            return Response.error(e.getMessage()).getMessage();
        }

        try {
            String result = aiDrawingService.userUploadHandIMG(stream, request, etc, contentType, request.getHouseSize());
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