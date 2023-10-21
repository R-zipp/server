package com.mtvs.arzip.controller;

import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.S3FileTestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
// 서버에서 사용 할 때 url : localhost:8080/upload
// 통신 때 사용 하는 url : IPv4주소:8080/upload
@RequiredArgsConstructor
@Api(tags = "파일 업로드, 다운로드 테스트")
public class S3FileTestController {

    private final S3FileTestService s3FileUploadTestService;

    @PostMapping
    public Response<String> uploadFile(@RequestPart("file")MultipartFile file) throws IOException {
        String url = s3FileUploadTestService.uploadFile(file);

        System.out.println("url" + url);
        return Response.success(url);
    }

    @GetMapping("/file_download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {
        return s3FileUploadTestService.getObject(fileName);
    }

}
