package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataUnrealDto;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.AiDrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/drawing")
public class AiDrawingController {

    private final AiDrawingService aiDrawingService;


    // 사용자가 올린 도면 정보 저장
    @PostMapping(value = "/process")
    public Response<?> processAndStoreImage(@RequestPart("file") MultipartFile file, @RequestPart AiDrawingDataUnrealDto aiDrawingDataDto  ) {
        try {
            aiDrawingService.userUploadFile(file, aiDrawingDataDto);
            return Response.success("이미지가 성공적으로 처리 및 저장되었습니다.");
        } catch (IOException e) {
            return Response.error("이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
        }

    }

    // 도면 정보 AI로 전송






}
