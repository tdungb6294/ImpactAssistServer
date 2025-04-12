package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.service.S3Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FileController {
    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/files/{filename}")
    public byte[] file(@PathVariable String filename) throws IOException {
        return s3Service.downloadFile(filename);
    }
}
