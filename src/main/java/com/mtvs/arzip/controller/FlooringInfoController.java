package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.flooring_info.FlooringInfoResponse;
import com.mtvs.arzip.domain.dto.flooring_info.FlooringStringDataRequest;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.FlooringInfoService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Api(tags = "바닥재 데이터 저장")
public class FlooringInfoController {

    private final FlooringInfoService flooringInfoService;

    @PostMapping("/save")
    public Response<FlooringInfoResponse> flooringSave(@RequestBody FlooringStringDataRequest request) {

        Long saveFlooringInfoNo;

        saveFlooringInfoNo = flooringInfoService.saveStringData(request);

        return Response.success(new FlooringInfoResponse("바닥재 정보 저장 완료", saveFlooringInfoNo));
    }

}