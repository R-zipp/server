package com.mtvs.arzip.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3FileTestService {

    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String dir = "/test1";

    private String downloadDir = "test1/";

    private String defaultUrl = "https://arzip-bucket.s3.ap-northeast-2.amazonaws.com";

    public String uploadFile(InputStream stream,  String contentType, String etc) throws IOException {

        String bucketDir = bucketName + dir;
        String dirUrl = defaultUrl + dir + "/";
        String fileName = generateFileName() + etc;

        log.info("🏠bucketDir : {}", bucketDir);
        log.info("🏠dirUrl : {}", dirUrl);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);

        amazonS3.putObject(bucketDir, fileName, stream, objectMetadata);
        return dirUrl + fileName;
    }

    public ResponseEntity<byte[]> getObject(String storedFileName) throws IOException {
        log.info("🏠파일 이름 : {}", storedFileName);
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, downloadDir + storedFileName));
        S3ObjectInputStream s3ObjectInputStream = ((S3Object) s3Object).getObjectContent();
        byte[] bytes = IOUtils.toByteArray(s3ObjectInputStream);

        log.info("🏠byte : {}", bytes);

        String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }


    private ObjectMetadata getObjectMetadata(InputStream fileStream, String contentType, long size) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(size);
        return objectMetadata;
    }

    private String generateFileName() {
        return UUID.randomUUID().toString();
    }



}
