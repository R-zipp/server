package com.mtvs.arzip.service;

import com.mtvs.arzip.domain.dto.ar_space_data.ARObjectPlacementDataRequest;
import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataRequest;
import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataResponse;
import com.mtvs.arzip.domain.entity.*;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ARSpaceDataService {

    private final ARSpaceDataRepository arSpaceDataRepository;
    private final UserRepository userRepository;
    private final AiDrawingRepository aiDrawingRepository;
    private final ARObjectPlacementDataRepository arObjectPlacementDataRepository;
    private final ObjectInfoRepository objectInfoRepository;

    @Transactional
    public Long saveSpaceData(ARSpaceDataRequest request, Principal principal) {

        Long id = Long.parseLong(principal.getName());
        log.info("🏠principal.getName() : {}", principal.getName());

        User user = userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUNDED));

//        Long aiDrawingDataNo = user.getLastUploadFloorPlanId();

//        AIDrawingData aiDrawingData = aiDrawingRepository.findById(aiDrawingDataNo)
//                .orElseThrow(()-> new AppException(ErrorCode.AI_DRAWING_DATA_NOT_FOUND));

        AIDrawingData aiDrawingData = aiDrawingRepository.findById(request.getAiDrawingDataNo())
                .orElseThrow(()-> new AppException(ErrorCode.AI_DRAWING_DATA_NOT_FOUND));

        // ARSpaceData 객체를 생성하여 저장
        ARSpaceData arSpaceData = request.toEntity(user, aiDrawingData);
        log.info("🏠arSpaceData.getUser().getNo() : {}", arSpaceData.getUser().getNo());
        log.info("🏠arSpaceData.getAiDrawingData().getFbxFile() : {}", arSpaceData.getAiDrawingData().getFbxFile());


        log.info("🏠arSpaceData.getUser : {}",arSpaceData.getUser().getEmail());
        log.info("🏠request.getUserNo : {}", request.getUserNo());

        arSpaceDataRepository.save(arSpaceData);

        log.info("🏠arSpaceData : {}",arSpaceData.getNo());

        // ARObjectPlacementData 객체들을 생성하여 저장
        for (ARObjectPlacementDataRequest placementDataRequest : request.getPlacements()) {

            ObjectInfo objectInfo = objectInfoRepository.findById(placementDataRequest.getObjectInfoNo())
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_INFO_NOT_FOUND));

            ARObjectPlacementData placementData = placementDataRequest.toEntity(arSpaceData, objectInfo);
            arObjectPlacementDataRepository.save(placementData);

            log.info("🏠placementData : {}", placementData.getArSpaceData().getNo());
        }

        log.info("🏠request.getPlacements().toString() : {}", request.getPlacements().toString());

        return arSpaceData.getNo();
    }

    @Transactional
    public ARSpaceDataResponse loadSpaceData(Long spaceNo, Principal principal) {

        Long id = Long.parseLong(principal.getName());


        ARSpaceData arSpaceData = arSpaceDataRepository.findById(spaceNo)
                .orElseThrow(()-> new AppException(ErrorCode.SPACE_NOT_FOUND));

        // 프로젝트를 생성한 사용자와 조회하는 사용자가 일치하는 경우는 조회수 증가하지 않음
        // 프로젝트가 공유되지 않았을 경우, 생성한 사용자만 조회 가능
        if (!arSpaceData.getUser().getNo().equals(id)) {
            // 프로젝트가 공유된 경우에만 조회할 수 있음
            if (!arSpaceData.isShared()) {
                throw new AppException(ErrorCode.UNSHARED_SPACE);
            }
            arSpaceData.addViews();
        } else {
            // 프로젝트를 생성한 사용자가 조회하는 경우, 조회수가 증가하지 않음
            if (!arSpaceData.isShared()) {
                return new ARSpaceDataResponse(arSpaceData, new ArrayList<>());
            }
        }

        // ARSpaceData에 연결된 ARObjectPlacementData 객체들을 조회
        List<ARObjectPlacementData> placements = arObjectPlacementDataRepository.findByArSpaceData(arSpaceData);

        // ARSpaceDataResponse 객체를 생성하여 반환
        return new ARSpaceDataResponse(arSpaceData, placements);
    }

    @Transactional
    public void share(Long spaceNo) {

        ARSpaceData arSpaceData = arSpaceDataRepository.findById(spaceNo)
                .orElseThrow(()-> new AppException(ErrorCode.SPACE_NOT_FOUND));

        arSpaceData.share();
    }



}
