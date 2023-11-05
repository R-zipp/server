package com.mtvs.arzip.service;

import com.mtvs.arzip.domain.dto.ar_space_data.ARObjectPlacementDataRequest;
import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataRequest;
import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataResponse;
import com.mtvs.arzip.domain.entity.*;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ARSpaceDataService {

    private final ARSpaceDataReposiroty arSpaceDataReposiroty;
    private final UserRepository userRepository;
    private final AiDrawingRepository aiDrawingRepository;
    private final ARObjectPlacementDataRepository arObjectPlacementDataRepository;
    private ObjectInfoRepository objectInfoRepository;

    @Transactional
    public Long saveSpaceData(ARSpaceDataRequest request) {

        User user = userRepository.findById(request.getUserNo())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUNDED));

        AIDrawingData aiDrawingData = aiDrawingRepository.findById(request.getAiDrawingDataNo())
                .orElseThrow(()-> new AppException(ErrorCode.AI_DRAWING_DATA_NOT_FOUND));

        // ARSpaceData 객체를 생성하여 저장
        ARSpaceData arSpaceData = request.toEntity(user, aiDrawingData);
        arSpaceDataReposiroty.save(arSpaceData);

        // ARObjectPlacementData 객체들을 생성하여 저장
        for (ARObjectPlacementDataRequest placementDataRequest : request.getPlacements()) {
            ObjectInfo objectInfo = objectInfoRepository.findById(placementDataRequest.getObjectInfoNo())
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_INFO_NOT_FOUND));

            ARObjectPlacementData placementData = placementDataRequest.toEntity(arSpaceData, objectInfo);
            arObjectPlacementDataRepository.save(placementData);
        }

        return arSpaceData.getNo();
    }

    @Transactional(readOnly = true)
    public ARSpaceDataResponse loadSpaceData(Long spaceNo) {

        ARSpaceData arSpaceData = arSpaceDataReposiroty.findById(spaceNo)
                .orElseThrow(()-> new AppException(ErrorCode.SPACE_NOT_FOUND));

        // ARSpaceData에 연결된 ARObjectPlacementData 객체들을 조회
        List<ARObjectPlacementData> placements = arObjectPlacementDataRepository.findByArSpaceData(arSpaceData);

        // ARSpaceDataResponse 객체를 생성하여 반환
        return new ARSpaceDataResponse(arSpaceData, placements);
    }

}
