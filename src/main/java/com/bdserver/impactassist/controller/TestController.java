package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.UserPrincipal;
import com.bdserver.impactassist.repo.UserRepo;
import com.bdserver.impactassist.service.S3Service;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("hello")
public class TestController {
    private final UserRepo userRepo;
    private final S3Service s3Service;

    public TestController(UserRepo userRepo, S3Service s3Service) {
        this.userRepo = userRepo;
        this.s3Service = s3Service;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/world")
    public String hello2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return String.valueOf(userRepo.findByUsername(userPrincipal.getUsername()).getId());
        } else {
            throw new UsernameNotFoundException("Not authenticated");
        }
    }

    @PostMapping("/1")
    public String hello3(@RequestParam("file") List<MultipartFile> file) throws IOException {
        s3Service.uploadFile(file.getFirst(), UUID.randomUUID());
        return file.getFirst().getOriginalFilename();
    }

    @GetMapping("/2")
    public ResponseEntity<byte[]> hello4() throws IOException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(s3Service.downloadFile("test.txt"));
    }

    @GetMapping("/3")
    public URL hello5() {
        return s3Service.getSignedUrl("test.txt");
    }
}
