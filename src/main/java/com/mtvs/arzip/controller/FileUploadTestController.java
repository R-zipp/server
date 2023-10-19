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
@RequiredArgsConstructor
@Api(tags = "파일 업로드")
public class FileUploadTestController {

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
