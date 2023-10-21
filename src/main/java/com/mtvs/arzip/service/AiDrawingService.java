package com.mtvs.arzip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataUnrealDto;
import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.repository.AiDrawingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiDrawingService {

    private final AiDrawingRepository aiDrawingRepository;
    private final S3FileTestService s3Service;
    private final ObjectMapper objectMapper;



    public void userUploadFile(MultipartFile imageFile, AiDrawingDataUnrealDto aiDrawingDataDto) throws IOException {

        log.info("🏠서비스 코드 시작");

        // 이미지 파일을 S3에 업로드 (이미지 저장 코드)
        String s3ImageUrl = s3Service.uploadFile(imageFile);
        log.info("🏠사용자가 전송한 이미지 S3 url: {}", s3ImageUrl);

        // AiDrawingDataDto를 엔티티로 변환
        AIDrawingData aiDrawingData = AiDrawingDataUnrealDto.toEntity(aiDrawingDataDto);
        log.info("🏠aiDrawingDataDto를 entity로 변환한 값 : {}", aiDrawingData);

        // AiDrawingData 엔티티를 저장
        aiDrawingRepository.save(aiDrawingData);

        log.info("🏠aiDrawingData: {}", aiDrawingData);
        log.info("🏠저장 완료");
    }


    private AiDrawingDataUnrealDto parseJsonData(String jsonImageData) {
        try {
            return objectMapper.readValue(jsonImageData, AiDrawingDataUnrealDto.class);
        } catch (IOException e) {
            // 예외 처리
            throw new RuntimeException("🏠JSON 데이터 파싱 중 오류 발생", e);
        }
    }

}
