package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.object_info.ObjectInfoResponse;
import com.mtvs.arzip.domain.dto.object_info.ObjectStringDataRequest;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.ObjectInfoService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/object")
@Api(tags = "오브젝트 데이터 저장")
public class ObjectInfoController {

    private final ObjectInfoService objectInfoService;

    // 오브젝트 타입, 이름 저장 용도
    @PostMapping("/save")
    public Response<ObjectInfoResponse> objectStringSave(@RequestBody ObjectStringDataRequest request) {
        Long savedObjectInfoNo;
        try {
            savedObjectInfoNo = objectInfoService.saveStringData(request);
        } catch (Exception e) {
            throw new AppException(ErrorCode.JSON_DATA_PARSING_ERROR);
        }

        return Response.success(new ObjectInfoResponse("Object 내용 저장 완료", savedObjectInfoNo));
    }

}
