package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataAIRequest;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataFloorPlanRequest;
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
    @PostMapping(value = "/process", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
//    @PostMapping(value = "/process", consumes = {MediaType.IMAGE_PNG_VALUE})
    public Response<?> processAndStoreImage(InputStream stream, AiDrawingDataFloorPlanRequest aiDrawingDataDto,
                                            @RequestHeader("Content-Type") String contentType) {

        System.out.println("contentType = " + contentType);
        System.out.println("stream = " + stream);

        String etc;
        try {
            etc = getfileExtension(contentType);
        } catch (UnsupportedOperationException e) {
            return Response.error(e.getMessage());
        }

        try {
            aiDrawingService.userUploadFloorPlan(stream, aiDrawingDataDto, etc, contentType);
            return Response.success("이미지가 성공적으로 처리 및 저장되었습니다.");
        } catch (IOException e) {
            return Response.error("이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    // AI가 올린 fbx 파일 정보 저장
    // fbx 파일 다운로드 후 Unreal로 전송
    @PutMapping(value = "/ai-upload/{no}", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public Response<?> aiUploadData(@PathVariable Long no, InputStream stream, @RequestPart AiDrawingDataAIRequest request,
                                    @RequestHeader("Content-Type")String contentType) {

        String etc = ".fbx";

        try {
            aiDrawingService.aiUploadFile(stream, request, no, etc, contentType);
            return Response.success("fbx 파일이 성공적으로 처리 및 저장되었습니다.");
        } catch (IOException e) {
            return Response.error("fbx 파일 처리 중 오류가 발생했습니다." + e.getMessage());
        }
    }


    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        try {
            byte[] fileResource = aiDrawingService.downloadFBXFileAsBytes(filename).getBody();
            HttpHeaders httpHeaders = aiDrawingService.createFileDownloadHeader(filename, fileResource);
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(fileResource);
        } catch (IOException e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String getfileExtension(String contentType) {
        switch (contentType) {
            case MediaType.IMAGE_JPEG_VALUE:
                return ".jpeg";

            case MediaType.IMAGE_PNG_VALUE:
                return ".png";

            default:
                throw new UnsupportedOperationException("UnSupported ContentType : " + contentType);
        }
    }

}