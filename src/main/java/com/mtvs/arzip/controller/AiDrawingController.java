package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataAIDto;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataUnrealDto;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.AiDrawingService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/drawing")
@Api(tags = "도면 전송 및 저장")
public class AiDrawingController {

    private final AiDrawingService aiDrawingService;


    // 사용자가 올린 도면 정보 저장, 데이터 전송
    @PostMapping(value = "/process")
    public Response<?> processAndStoreImage(@RequestPart("file") MultipartFile file, @RequestPart AiDrawingDataUnrealDto aiDrawingDataDto  ) {
        try {
            aiDrawingService.userUploadFile(file, aiDrawingDataDto);
            return Response.success("이미지가 성공적으로 처리 및 저장되었습니다.");
        } catch (IOException e) {
            return Response.error("이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
        }

    }

    // AI가 올린 fbx 파일 정보 저장
    @PostMapping(value = "/ai-upload/{no}")
    public Response<?> aiUploadData(@PathVariable Long no, @RequestPart("file") MultipartFile file, @RequestPart AiDrawingDataAIDto aiDrawingDataAIDto) {
        try {
            aiDrawingService.aiUploadFile(file, aiDrawingDataAIDto, no);
            return Response.success("fbx 파일이 성공적으로 처리 및 저장되었습니다.");
        } catch (IOException e) {
            return Response.error("fbx 파일 처리 중 오류가 발생했습니다." + e.getMessage());
        }
    }

    // fbx 파일 다운로드 후 Unreal로 전송
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





}
