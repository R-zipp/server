package com.mtvs.arzip.service;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtvs.arzip.domain.dto.ai_drawing_data.*;
import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.repository.AiDrawingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiDrawingService {

    private final AiDrawingRepository aiDrawingRepository;
    private final S3FileTestService s3Service;
    private final ObjectMapper objectMapper;


    // 유저가 올린 손 도면 이미지 타입, url 저장
    public String userUploadFloorPlan(InputStream stream, AiDrawingDataFloorPlanRequest request, String etc, String contentType, String houseSize, int wallPaperNo) throws IOException {
        return userUpload(stream, request, (s, r) -> AiDrawingDataFloorPlanRequest.toEntity((AiDrawingDataFloorPlanRequest) r), etc, contentType, houseSize, wallPaperNo);
    }

    // 유저가 올린 일반 도면 이미지 타입, url 저장
    public String userUploadHandIMG(InputStream stream, AiDrawingDataHandingRequest request, String etc, String contentType, String houseSize, int wallPaperNo) throws IOException {
        return userUpload(stream, request, (s, r) -> AiDrawingDataHandingRequest.toEntity((AiDrawingDataHandingRequest) r), etc, contentType, houseSize, wallPaperNo);
    }


    // 사용자가 올린 도면 이미지 db ai로 전송
    private AiDrawingDataResponse sendDrawingDataToAI(AiDrawingDataResponse aiDrawingDataResponse, AiDrawingDataAIRequest request) throws IOException {
        log.info("🏠AI로 데이터 전송 서비스 코드 시작");

        WebClient webClient = WebClient.builder().baseUrl("https://936a-221-163-19-218.ngrok-free.app").build();

        try {
            // AI 서버로부터 S3 URL을 받아옴
            AiResponse aiResponse = webClient.post()
                    .uri("/spring/img_to_fbx_S3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(aiDrawingDataResponse))
                    .retrieve()
                    .bodyToMono(AiResponse.class)
                    .block();

            String URL = aiResponse.getURL();

            log.info("🏠AI와 통신 성공");
            log.info("🏠AI로부터 받은 S3 URL : " + aiResponse.getURL());

            // s3 URL 저장
            request.setFbxFile(URL);

            // AiDrawingDataResponse 객체 생성 후 반환
            AiDrawingDataResponse response = new AiDrawingDataResponse();
            response.setFbxFile(URL);

            return response;

        } catch(WebClientResponseException e) {
            log.error("🏠AI와 통신 실패", e);
            log.error("🏠오류 상태 코드 : " + e.getRawStatusCode());
            log.error("🏠오류 메시지 : " + e.getResponseBodyAsString());
            throw new RuntimeException("🏠AI와 통신 실패(응답 오류)", e);
        }
    }

    @Transactional
    // BiFunction<InputStream, Object, AIDrawingData> toEntity
    // 이 매개변수는 두 개의 입력값(InputStream과 Object)을 받아 AIDrawingData 타입의 결과를 반환하는 함수
    // 두 개의 입력값을 받아 결과를 반환하는 메소드를 가지고 있다.

    public String userUpload(InputStream stream, Object request, BiFunction<InputStream, Object, AIDrawingData> toEntity, String etc, String contentType, String houseSize, int wallPaperNo) throws IOException {
        log.info("🏠AiDrawing 서비스 코드 시작");

        System.out.println("stream = " + stream);

        // InputStream으로부터 byte 배열 읽어오기
        // IOUtils.toByteArray() 메서드 : InputStream을 이용하여 byte 배열로 변환
        byte[] bytes = IOUtils.toByteArray(stream);

        // byte 배열을 Base64로 인코딩하여 문자열로 변환
        // 바이너리 데이터를 그대로 처리하면, 데이터의 손실이나 변형이 발생할 수 있기 때문에 바이너리 데이터를 텍스트 형태로 변환한 후 전송하는 것이 일반적
        // Base64.getEncoder().encodeToString() 메서드를 사용하여 byte 배열을 Base64 문자열로 변환
        String base64String = Base64.getEncoder().encodeToString(bytes);

        // Base64 문자열을 디코딩하여 다시 byte 배열로 변환
        // 인코딩된 데이터를 실제로 사용하려면 원래의 바이너리 형태로 복원해야 한다.
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

        // request 별 S3에 올라간 이미지 url 저장
        if (request instanceof AiDrawingDataFloorPlanRequest) {
            ((AiDrawingDataFloorPlanRequest) request).setUserDrawingImage(s3ImageUrl);
        } else if (request instanceof AiDrawingDataHandingRequest) {
            ((AiDrawingDataHandingRequest) request).setUserDrawingImage(s3ImageUrl);
        }

        // request를 엔티티로 변환
        AIDrawingData aiDrawingData = toEntity.apply(stream, request);
        log.info("🏠request를 entity로 변환한 값 : {}", aiDrawingData.getNo());

        aiDrawingRepository.save(aiDrawingData);
        log.info("🏠aiDrawingData: {}", aiDrawingData);
        log.info("🏠저장 완료");

        // AiDrawingDataResponse 객체 생성
        AiDrawingDataResponse aiDrawingDataResponse = new AiDrawingDataResponse();
        aiDrawingDataResponse.setUserDrawingImage(s3ImageUrl);
        aiDrawingDataResponse.setHouseSize(houseSize);
        aiDrawingDataResponse.setDrawingType(aiDrawingData.getDrawingType().name());
        aiDrawingDataResponse.setWallPaperNo(wallPaperNo);

        // AiDrawingDataAIRequest 객체 생성
        AiDrawingDataAIRequest aiDrawingDataAIRequest = new AiDrawingDataAIRequest();
        aiDrawingDataAIRequest.setHouseSize(houseSize);
        aiDrawingDataAIRequest.setWallPaperNo(wallPaperNo);
        if (request instanceof AiDrawingDataHandingRequest) {
            aiDrawingDataAIRequest.setDrawingType(((AiDrawingDataHandingRequest) request).getDrawingType());
        } else if (request instanceof AiDrawingDataFloorPlanRequest) {
            aiDrawingDataAIRequest.setDrawingType(((AiDrawingDataFloorPlanRequest) request).getDrawingType());
        }


        log.info("🏠AI로 전송 시작");
        AiDrawingDataResponse result = sendDrawingDataToAI(aiDrawingDataResponse, aiDrawingDataAIRequest);

        // AI 서버로부터 받은 FBX 파일의 S3 URL을 AiDrawingData 엔티티에 저장
        aiDrawingData.updateFbxFile(result.getFbxFile());

        // entity 저장
        aiDrawingRepository.save(aiDrawingData);
        log.info("🏠FBX 파일 URL 저장 완료");

        return result.getFbxFile();
    }

    private AiDrawingDataFloorPlanRequest parseJsonData(String jsonImageData) {
        try {
            return objectMapper.readValue(jsonImageData, AiDrawingDataFloorPlanRequest.class);
        } catch (IOException e) {
            // 예외 처리
            throw new AppException(ErrorCode.JSON_DATA_PARSING_ERROR);
        }
    }


}