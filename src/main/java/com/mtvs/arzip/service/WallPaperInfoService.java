package com.mtvs.arzip.service;

import com.mtvs.arzip.domain.dto.wall_paper_info.WallPaperStringDataRequest;
import com.mtvs.arzip.domain.entity.WallPaperInfo;
import com.mtvs.arzip.repository.WallPaperInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WallPaperInfoService {

    private final WallPaperInfoRepository wallPaperInfoRepository;

    @Transactional
    public Long saveStringData(WallPaperStringDataRequest request) {

        // wallPaperInfo 객체를 생성하여 저장
        WallPaperInfo wallPaperInfo = new WallPaperInfo(request.getWallPaperType());
        wallPaperInfoRepository.save(wallPaperInfo);
        log.info("🏠데이터 저장 완료 : {}", wallPaperInfo);

        return wallPaperInfo.getNo(); // 저장된 wallPaperInfo의 ID를 반환
    }

}
