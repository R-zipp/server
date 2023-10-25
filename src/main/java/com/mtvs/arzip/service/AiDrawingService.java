package com.mtvs.arzip.service;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AIDrawingDataDto;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataAIRequest;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataFloorPlanRequest;
import com.mtvs.arzip.domain.dto.ai_drawing_data.AiDrawingDataResponse;
import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.repository.AiDrawingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiDrawingService {

    private final AiDrawingRepository aiDrawingRepository;
    private final S3FileTestService s3Service;
    private final ObjectMapper objectMapper;


   // 유저가 올린 도면 이미지 타입, url 저장
    @Transactional
    public void userUploadFloorPlan(InputStream stream, AiDrawingDataFloorPlanRequest request, String etc, String contentType) throws IOException {

        log.info("🏠AiDrawing 서비스 코드 시작");

        System.out.println("stream = " + stream);

        // InputStream으로부터 byte 배열 읽어오기
        // IOUtils.toByteArray() 메서드 : InputStream을 이용하여 byte 배열로 변환
        byte[] bytes = IOUtils.toByteArray(stream);

        // byte 배열을 Base64로 인코딩하여 문자열로 변환
        // Base64.getEncoder().encodeToString() 메서드를 사용하여 byte 배열을 Base64 문자열로 변환
        String base64String = Base64.getEncoder().encodeToString(bytes);

        // Base64 문자열을 디코딩하여 다시 byte 배열로 변환
        // "," 이후의 문자열을 추출하여 org.apache.commons.codec.binary.Base64.decodeBase64() 메서드를 사용하여 Base64 문자열을 디코딩하고, 다시 byte 배열로 변환
        // Base64 문자열이 "data:image/png;base64,"와 같은 형식으로 시작하는 경우, 실제 이미지 데이터는 "," 이후의 부분에 위치하므로, 해당 부분만 추출하여 디코딩
        byte[] base64Bytes = org.apache.commons.codec.binary.Base64.decodeBase64((base64String.substring(base64String.indexOf(",")+1)).getBytes());

        // 디코딩된 byte 배열을 이용하여 ByteArrayInputStream을 생성
        // ByteArrayInputStream : byte 배열을 읽기 위한 InputStream
        // 생성된 inputStreamForUpload를 이후의 로직에서 사용하여 이미지 데이터를 업로드하거나 처리 가능
        ByteArrayInputStream inputStreamForUpload = new ByteArrayInputStream(base64Bytes);

        // 이미지 파일을 S3에 업로드 (이미지 저장 코드)
        String s3ImageUrl = s3Service.uploadFile(inputStreamForUpload, contentType, etc);
        log.info("🏠사용자가 전송한 이미지 S3 url: {}", s3ImageUrl);

        // S3에 올라간 이미지 url 저장
        request.setUserDrawingImage(s3ImageUrl);

        // AiDrawingDataDto를 엔티티로 변환
        AIDrawingData aiDrawingData = AiDrawingDataFloorPlanRequest.toEntity(request);
        log.info("🏠AiDrawingDataUnrealDto를 entity로 변환한 값 : {}", aiDrawingData.getNo());

        // AiDrawingData 엔티티를 저장
        aiDrawingRepository.save(aiDrawingData);
        log.info("🏠aiDrawingData: {}", aiDrawingData);
        log.info("🏠저장 완료");

//        log.info("🏠AI로 전송 시작");
//        sendDrawingDataToAI(aiDrawingDataFloorPlanDto);

    }


    // 사용자가 올린 도면 이미지 db ai로 전송
    private void sendDrawingDataToAI(AiDrawingDataResponse aiDrawingDataResponse) {
        //DTO 분리()
        log.info("🏠AI로 데이터 전송 서비스 코드 시작");

        WebClient webClient = WebClient.create();

        // AI 요청 URL
        String aiUrl = "";

        webClient.post()
                .uri(aiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(aiDrawingDataResponse))
                .exchange()
                .subscribe(
                        result -> {
                            if (result.statusCode().is2xxSuccessful()) {
                                log.info("🏠AI 서비스와 통신 성공");
                                System.out.println(result);
//                                 aiUploadFile(result.)
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
    public AIDrawingDataDto aiUploadFile(InputStream stream, AiDrawingDataAIRequest request, Long no, String etc, String contentType) throws IOException {
        // 파일을 ai에게
        // 파일을 받으면 s3에 서버가 업로드
        log.info("🏠서비스 코드 시작");

        AIDrawingData aiDrawingData = aiDrawingRepository.findById(no)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));
        log.info("🏠aiDrawingData : {}", aiDrawingData.getNo());

        byte[] bytes = IOUtils.toByteArray(stream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        log.info("🏠bufferedImage: {}", bufferedImage);


        ByteArrayInputStream inputStreamForUpload = new ByteArrayInputStream(bytes);

        // 이미지 파일을 S3에 업로드 (이미지 저장 코드)
        String s3FbxUrl = s3Service.uploadFile(inputStreamForUpload, contentType, etc);
        log.info("🏠AI가 전송한 이미지 S3 url: {}", s3FbxUrl);

        request.setFbxFile(s3FbxUrl);

        AIDrawingData saved = AIDrawingData.builder()
                .no(aiDrawingData.getNo())
                .userDrawingImage(aiDrawingData.getUserDrawingImage())
                .drawingType(aiDrawingData.getDrawingType())
                .fbxFile(request.getFbxFile())
                .build();

        // AiDrawingDataDto를 엔티티로 변환
        aiDrawingData = AiDrawingDataAIRequest.toEntity(request);
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


    private AiDrawingDataFloorPlanRequest parseJsonData(String jsonImageData) {
        try {
            return objectMapper.readValue(jsonImageData, AiDrawingDataFloorPlanRequest.class);
        } catch (IOException e) {
            // 예외 처리
            throw new RuntimeException("🏠JSON 데이터 파싱 중 오류 발생", e);
        }
    }

}
