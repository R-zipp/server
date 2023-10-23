package com.mtvs.arzip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AIDrawingDataDto;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataAIDto;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataUnrealDto;
import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.repository.AiDrawingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiDrawingService {

    private final AiDrawingRepository aiDrawingRepository;
    private final S3FileTestService s3Service;
    private final ObjectMapper objectMapper;


   // 유저가 올린 도면 이미지 타입, url 저장
    @Transactional
    public void userUploadFile(MultipartFile imageFile, AiDrawingDataUnrealDto aiDrawingDataUnrealDto) throws IOException {

        log.info("🏠서비스 코드 시작");

        // 이미지 파일을 S3에 업로드 (이미지 저장 코드)
        String s3ImageUrl = s3Service.uploadFile(imageFile);
        log.info("🏠사용자가 전송한 이미지 S3 url: {}", s3ImageUrl);

        // S3에 올라간 이미지 url 저장
        aiDrawingDataUnrealDto.setUserDrawingImage(s3ImageUrl);

        // AiDrawingDataDto를 엔티티로 변환
        AIDrawingData aiDrawingData = AiDrawingDataUnrealDto.toEntity(aiDrawingDataUnrealDto);
        log.info("🏠AiDrawingDataUnrealDto를 entity로 변환한 값 : {}", aiDrawingData.getNo());

        // AiDrawingData 엔티티를 저장
        aiDrawingRepository.save(aiDrawingData);
        log.info("🏠aiDrawingData: {}", aiDrawingData);
        log.info("🏠저장 완료");


        log.info("🏠AI로 전송 시작");
        sendDrawingDataToAI(aiDrawingDataUnrealDto);



    }

    // 사용자가 올린 도면 이미지 db ai로 전송
    private void sendDrawingDataToAI(AiDrawingDataUnrealDto aiDrawingDataUnrealDto) {
        //DTO 분리
        log.info("🏠AI로 데이터 전송 서비스 코드 시작");

        WebClient webClient = WebClient.create();

        // AI 요청 URL
        String aiUrl = "https://920c-221-163-19-218.ngrok-free.app/test_api/in_json_out_json";

        webClient.post()
                .uri(aiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(aiDrawingDataUnrealDto))
                .exchange()
                .subscribe(
                        result -> {
                            if (result.statusCode().is2xxSuccessful()) {
                                log.info("🏠AI 서비스와 통신 성공");
                            } else {
                                log.error("🏠AI 서비스 응답 오류: " + result.statusCode());
                            }
                        },
                        error -> {
                            if (error instanceof AppException) {
                                AppException appException = (AppException) error;
                                log.error("🏠AI 서비스 오류: " + appException.getErrorCode().getMessage());
                            } else {
                                log.error("🏠알 수 없는 오류 발생: " + error.getMessage());
                            }
                        }
                );

        log.info("🏠webClient : {}", webClient);



    }

    // ai에서 보낸 도면 데이터 저장
    @Transactional
    public AIDrawingDataDto aiUploadFile(MultipartFile file, AiDrawingDataAIDto aiDrawingDataAIDto, Long no) throws IOException {

        log.info("🏠서비스 코드 시작");

        AIDrawingData aiDrawingData = aiDrawingRepository.findById(no)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));
        log.info("🏠aiDrawingData : {}", aiDrawingData.getNo());


        // 이미지 파일을 S3에 업로드 (이미지 저장 코드)
        String s3FbxUrl = s3Service.uploadFile(file);
        log.info("🏠AI가 전송한 이미지 S3 url: {}", s3FbxUrl);

        aiDrawingDataAIDto.setFbxFile(s3FbxUrl);

        AIDrawingData saved = AIDrawingData.builder()
                .no(aiDrawingData.getNo())
                .userDrawingImage(aiDrawingData.getUserDrawingImage())
                .drawingType(aiDrawingData.getDrawingType())
                .fbxFile(aiDrawingDataAIDto.getFbxFile())
                .build();

        // AiDrawingDataDto를 엔티티로 변환
        aiDrawingData = AiDrawingDataAIDto.toEntity(aiDrawingDataAIDto);
        log.info("🏠aiDrawingDataAIDto를 entity로 변환한 값 : {}", aiDrawingData.getNo());

        aiDrawingData.modify(s3FbxUrl);

        // AiDrawingData 엔티티를 저장
        AIDrawingData savedAiDrawingData = aiDrawingRepository.save(saved);
        //log.info("🏠savedAiDrawingData: {}", savedAiDrawingData.getNo());
        log.info("🏠저장 완료");

        return AIDrawingDataDto.of(savedAiDrawingData);
//        return AIDrawingDataDto.of(aiDrawingData);
    }

    // fbx 파일 다운로드 후 unreal로 전송
    public ResponseEntity<byte[]> downloadFBXFileAsBytes(String filename) {
        try {
            // S3에서 파일 다운
            ResponseEntity<byte[]> responseEntity = s3Service.getObject(filename);
            byte[] fileData = responseEntity.getBody();
            log.info("🏠fileData : {}", fileData);

            // HttpHeaders를 설정하여 파일 다운로드 응답을 생성
            HttpHeaders httpHeaders = createFileDownloadHeader(filename, fileData);

            return new ResponseEntity<>(fileData, httpHeaders, HttpStatus.OK);
        } catch (IOException e) {
            // 파일을 읽어오는 중 오류가 발생하면 예외 처리
            throw new RuntimeException("🏠파일 다운로드 중 오류 발생", e);
        }
    }

    // 파일 다운로드
    public HttpHeaders createFileDownloadHeader(String filename, byte[] fileData) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        log.info("🏠encodedFileName : {}", encodedFileName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(fileData.length);
        httpHeaders.setContentDispositionFormData("attachment", encodedFileName);

        return httpHeaders;
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
