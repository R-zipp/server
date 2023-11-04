package com.mtvs.arzip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ObjectInfoService {

    private final ObjectInfoRepository objectInfoRepository;
    private final ObjectMapper objectMapper;


    @Transactional
    public Long saveStringData(ObjectStringDataRequest request) {

        // ObjectInfo 객체를 생성하여 저장
        ObjectInfo objectInfo = new ObjectInfo(request.getObjectName(), ObjectType.valueOf(request.getObjectType()));
        objectInfoRepository.save(objectInfo);
        log.info("🏠데이터 저장 완료 : {}", objectInfo);

        return objectInfo.getNo(); // 저장된 ObjectInfo의 ID를 반환
    }

}
