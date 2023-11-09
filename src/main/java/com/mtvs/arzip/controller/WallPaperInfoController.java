package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.wall_paper_info.WallPaperResponse;
import com.mtvs.arzip.domain.dto.wall_paper_info.WallPaperStringDataRequest;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.WallPaperInfoService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wall-paper")
@RequiredArgsConstructor
@Api(tags = "벽지 데이터 저장")
public class WallPaperInfoController {

    private final WallPaperInfoService wallPaperInfoService;

    @PostMapping("/save")
    public Response<WallPaperResponse> wallPaperSave(@RequestBody WallPaperStringDataRequest request) {

        Long savedWallPaperInfoNo;

        savedWallPaperInfoNo = wallPaperInfoService.saveStringData(request);

        return Response.success(new WallPaperResponse("벽지 정보 저장 완료", savedWallPaperInfoNo));

    }

}
