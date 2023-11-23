package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataRequest;
import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceDataResponse;
import com.mtvs.arzip.domain.dto.ar_space_data.ARSpaceListResponse;
import com.mtvs.arzip.domain.dto.object_info.ObjectInfoResponse;
import com.mtvs.arzip.domain.entity.User;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.ARSpaceDataService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/space")
@RequiredArgsConstructor
@Api(tags = "공간 정보 저장 및 조회")
public class ARSpaceDataController {

    private final ARSpaceDataService arSpaceDataService;


    @PostMapping("/save")
    public Response<ObjectInfoResponse> saveSpace(@RequestBody ARSpaceDataRequest request, Principal principal, HttpServletRequest httpServletRequest) {
        Long savedSpaceNo;

        savedSpaceNo = arSpaceDataService.saveSpaceData(request, principal, httpServletRequest);

        return Response.success(new ObjectInfoResponse("AR 공간 저장 완료", savedSpaceNo));
    }

    @PostMapping("/load")
    public Response<ARSpaceDataResponse> loadSpace(@RequestParam Long spaceNo, Principal principal) {
        ARSpaceDataResponse response;

        Long id = Long.parseLong(principal.getName());

        response = arSpaceDataService.loadSpaceData(spaceNo, principal);

        return Response.success(response);
    }

    @PutMapping("/share/{spaceNo}")
    public Response<?> share(@PathVariable Long spaceNo) {
        arSpaceDataService.share(spaceNo);
        return Response.success("공간 공유 완료");
    }

    @PostMapping("/list")
    public Response<Page<ARSpaceListResponse>> list(@PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {
        Page<ARSpaceListResponse> listResponses = arSpaceDataService.loadMyList(principal, pageable);
        return Response.success(listResponses);
    }

}
