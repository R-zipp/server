package com.mtvs.arzip.controller;

import com.amazonaws.util.IOUtils;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.S3FileTestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Api(tags = "파일 업로드, 다운로드 테스트")
public class S3FileTestController {

    private final S3FileTestService s3FileUploadTestService;

    @PostMapping(value = "/test", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Response<String> uploadFile(InputStream stream, @RequestHeader("Content-Type") String contentType) throws IOException {

        System.out.println("contentType = " + contentType);
        System.out.println("stream = " + stream);

        String etc = "";
        switch (contentType) {
            case MediaType.IMAGE_JPEG_VALUE:
                etc = ".jpeg";

            case MediaType.IMAGE_PNG_VALUE:
                etc = ".png";
        }

        byte[] bytes = IOUtils.toByteArray(stream);

        ByteArrayInputStream inputStreamForImageProcessing = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(inputStreamForImageProcessing);

        ByteArrayInputStream inputStreamForUpload = new ByteArrayInputStream(bytes);

        String url = s3FileUploadTestService.uploadFile(inputStreamForUpload, contentType, etc);

        System.out.println("url" + url);
        return Response.success(url);
    }

    @GetMapping("/file_download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {
        return s3FileUploadTestService.getObject(fileName);
    }

}
