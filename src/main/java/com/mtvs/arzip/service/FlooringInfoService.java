package com.mtvs.arzip.service;

import com.mtvs.arzip.domain.dto.flooring_info.FlooringStringDataRequest;
import com.mtvs.arzip.domain.entity.FlooringInfo;
import com.mtvs.arzip.repository.FlooringInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlooringInfoService {

    private final FlooringInfoRepository flooringInfoRepository;

    @Transactional
    public Long saveStringData(FlooringStringDataRequest request) {

        // flooringInfo 객체를 생성하여 저장
        FlooringInfo flooringInfo = new FlooringInfo(request.getFlooringType());
        flooringInfoRepository.save(flooringInfo);
        log.info("🏠데이터 저장 완료 : {}", flooringInfo);

        return flooringInfo.getNo(); // 저장된 flooringInfo의 ID를 반환
    }

}
