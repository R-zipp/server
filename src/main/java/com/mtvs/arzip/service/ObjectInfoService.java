package com.mtvs.arzip.service;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtvs.arzip.domain.dto.object_info.ObjectInfoDto;
import com.mtvs.arzip.domain.dto.object_info.ObjectFileDataRequest;
import com.mtvs.arzip.domain.dto.object_info.ObjectStringDataRequest;
import com.mtvs.arzip.domain.entity.ObjectInfo;
import com.mtvs.arzip.domain.enum_class.ObjectType;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.repository.ObjectInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ObjectInfoService {

    private final ObjectInfoRepository objectInfoRepository;
    private final S3FileTestService s3Service;
    private final ObjectMapper objectMapper;

    @Transactional
    public ObjectInfoDto objectFileSave(InputStream stream, ObjectFileDataRequest objectInfoRequest, String etc, String contentType) throws IOException {

        log.info("🏠ObjectInfo 서비스 코드 시작");

        byte[] bytes;

        try {
            bytes = IOUtils.toByteArray(stream);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        String base64String = Base64.getEncoder().encodeToString(bytes);

        byte[] base64Bytes = org.apache.commons.codec.binary.Base64.decodeBase64((base64String.substring(base64String.indexOf(",")+1)).getBytes());

        ByteArrayInputStream inputStreamForUpload = new ByteArrayInputStream(base64Bytes);

        String s3ImageUrl = s3Service.objectUpload(inputStreamForUpload, contentType, etc);
        log.info("🏠저장된 오브젝트 S3 url: {}", s3ImageUrl);

        objectInfoRequest.setObjectImage(s3ImageUrl);

        ObjectInfo objectInfo = ObjectFileDataRequest.toEntity(objectInfoRequest);

        try {
            objectInfoRepository.save(objectInfo);
            log.info("🏠objectInfo_No: {}", objectInfo.getNo());
            log.info("🏠object 파일 저장 완료");
        } catch (Exception e) {
            throw new AppException(ErrorCode.DATABASE_ERROR);
        }

        return ObjectInfoDto.of(objectInfo);
    }

    @Transactional
    public Long saveStringData(String jsonData, Long no) {

        ObjectInfo objectInfo = objectInfoRepository.findById(no)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND));

        try{
            // Json 데이터를 ObjectStringDataRequest 객체로 변환
            ObjectStringDataRequest request = objectMapper.readValue(jsonData, ObjectStringDataRequest.class);

            // ObjectInfo 객체로 변환하여 저장
            objectInfo.update(request.getObjectName(), ObjectType.valueOf(request.getObjectType()));
            objectInfoRepository.save(objectInfo);
            log.info("🏠데이터 저장 완료 : {}", objectInfo);

        } catch (IOException e) {
            throw new AppException(ErrorCode.JSON_DATA_PARSING_ERROR);
        }

        return no;

    }
}
