package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataRequest;
import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataResponse;
import com.mtvs.arzip.domain.dto.object_info.ObjectInfoResponse;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.ARSpaceDataService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/space")
@RequiredArgsConstructor
@Api(tags = "공간 정보 저장 및 조회")
public class ARSpaceDataController {

    private final ARSpaceDataService arSpaceDataService;


    @PostMapping("/save")
    public Response<ObjectInfoResponse> saveSpace(@RequestBody ARSpaceDataRequest request) {
        Long savedSpaceNo;
        try {
            savedSpaceNo = arSpaceDataService.saveSpaceData(request);
        } catch (Exception e) {
            throw new AppException(ErrorCode.SPACE_DATA_SAVING_ERROR);
        }

        return Response.success(new ObjectInfoResponse("AR 공간 저장 완료", savedSpaceNo));
    }


}
